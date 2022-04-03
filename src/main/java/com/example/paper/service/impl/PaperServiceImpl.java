package com.example.paper.service.impl;


import com.example.paper.dao.PaperRepository;
import com.example.paper.entity.Author;
import com.example.paper.entity.Fos;
import com.example.paper.entity.Paper;
import com.example.paper.entity.Venue;
import com.example.paper.entity.vo.PaperSummaryVO;
import com.example.paper.exception.BadReqException;
import com.example.paper.service.PaperService;
import com.example.paper.utils.CookieUtils;
import com.example.paper.utils.PageHelper;
import com.example.paper.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class PaperServiceImpl implements PaperService {
    private static final int SUMMARY_LIMIT=5;
    private static final int MAX_RESULT_SIZE=1000;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private CookieUtils cookieUtils;

    @Autowired
    private PageHelper pageHelper;

    @Override
    @Cacheable(cacheNames = "queryPaper", unless = "#result==null")
    public List<Paper> queryPaper(final String key, final String returnFacets,
                                     int pageNum, int pageSize,HttpServletRequest request,
                                  HttpServletResponse response) {
        Pageable pageable= PageRequest.of(pageNum,pageSize);
        Pageable wholePageable=PageRequest.of(0,MAX_RESULT_SIZE);
        //TODO: 后期通过先到es中查询匹配数量，告知用户总匹配数，然后只显示前n条
        List<Paper> paperList;
        List<Paper> wholeList;
        if(returnFacets.equals("all")){
            wholeList=paperRepository.findByKey(key,wholePageable);
            paperList=paperRepository.findByKey(key,pageable);
        }
        else if(returnFacets.equals("title")){
            wholeList=paperRepository.findByKey(key,"title",wholePageable);
            paperList=paperRepository.findByKey(key,"title",pageable);
        }
        else if(returnFacets.equals("authors")){
            wholeList=paperRepository.findByKey(key,"authors.name",wholePageable);
            paperList=paperRepository.findByKey(key,"authors.name",pageable);
        }
        else if(returnFacets.equals("conferences")){
            wholeList=paperRepository.findByKey(key,"venue.raw",wholePageable);
            paperList=paperRepository.findByKey(key,"venue.raw",pageable);
        }
        else if(returnFacets.equals("terms")){
            wholeList=paperRepository.findByKey(key,"fos.name",wholePageable);
            paperList=paperRepository.findByKey(key,"fos.name",pageable);
        }
        else{
            return null;
        }
        String qid = UUID.randomUUID().toString().replaceAll("-", "");
        request.getSession().setAttribute(qid,wholeList);
        cookieUtils.set(response,"qid",qid);
        return paperList;
    }

    @Override
    @Cacheable(cacheNames = "papersSummary", unless = "#result==null")
    public PaperSummaryVO papersSummary(String qid, HttpServletRequest request) {
        List<Paper> wholeList=(List<Paper>) request.getSession().getAttribute(qid);
        Map<String,Integer> termCount=new HashMap<>();
        Map<String,Integer> authorCount=new HashMap<>();
        Map<String,Integer> conferenceCount=new HashMap<>();
        Map<String,Integer> publisherCount=new HashMap<>();
        for(Paper p:wholeList){
            Fos[] foss=p.getFos();
            for(Fos fos:foss){
                termCount.put(fos.getName(),termCount.getOrDefault(fos.getName(),0)+1);
            }

            Author[] authors=p.getAuthors();
            for(Author author:authors){
                authorCount.put(author.getName(),authorCount.getOrDefault(author.getName(),0)+1);
            }

            Venue venue=p.getVenue();
            conferenceCount.put(venue.getRaw(),conferenceCount.getOrDefault(venue.getRaw(),0)+1);

            String publisher=p.getPublisher();
            publisherCount.put(publisher,publisherCount.getOrDefault(publisher,0)+1);
        }

        List<Pair<String,Integer>> termList=transformHash(termCount,SUMMARY_LIMIT);
        List<Pair<String,Integer>> authorList=transformHash(authorCount,SUMMARY_LIMIT);
        List<Pair<String,Integer>> conferenceList=transformHash(conferenceCount,SUMMARY_LIMIT);
        List<Pair<String,Integer>> publisherList=transformHash(publisherCount,SUMMARY_LIMIT);

        return new PaperSummaryVO(termList,authorList,conferenceList,publisherList);
    }

    @Override
    public List<Paper> queryPaperRefine(String qid, List<String> refinements, int pageNum, int pageSize, HttpServletRequest request) {
        List<Paper> wholeList=(List<Paper>)request.getSession().getAttribute(qid);
        List<Paper> refineList=paperRefine(wholeList,refinements);

        List<Paper> paperList=pageHelper.of(refineList,pageSize,pageNum);
        System.out.println("refine 分页前大小"+refineList.size());
        System.out.println("refine 分页后大小"+paperList.size());
        return paperList;
    }

    private List<Paper> paperRefine(List<Paper> papers,List<String> refinements){
        Map<String,List<String>> refinementsMap=refineAnalyze(refinements);
        if(papers==null||papers.isEmpty()) return papers;
        if(refinementsMap.containsKey("year")){
            papers=papers.stream().filter((Paper paper)->{
                String[] year_range=refinementsMap.get("year").get(0).split("_");
                if(year_range.length!=2) throw new BadReqException();
                int start=Integer.parseInt(year_range[0]),end=Integer.parseInt(year_range[1]);
                int year=paper.getYear();
                return year<=end&&year>=start;

            }).collect(Collectors.toList());
            if(papers.isEmpty()){
                return papers;
            }
        }

        if(refinementsMap.containsKey("author")){
            papers=papers.stream().filter((Paper paper)->{
                Author[] authors=paper.getAuthors();
                for(String v:refinementsMap.get("author")){
                    for(Author author:authors){
                        if(author.getName().contains(v)){
                            return true;
                        }
                    }
                }
                return false;
            }).collect(Collectors.toList());
            if(papers.isEmpty()){
                return papers;
            }
        }

        if(refinementsMap.containsKey("term")){
            papers=papers.stream().filter((Paper paper)->{
                Fos[] foss=paper.getFos();
                for(String v:refinementsMap.get("term")){
                    for(Fos fos:foss){
                        if(fos.getName().contains(v)){
                            return true;
                        }
                    }
                }
                return false;
            }).collect(Collectors.toList());
            if(papers.isEmpty()){
                return papers;
            }
        }

        if(refinementsMap.containsKey("conference")){
            papers=papers.stream().filter((Paper paper)->{
                String conference=paper.getVenue().getRaw();
                for(String v:refinementsMap.get("conference")){
                    if(conference.contains(v)){
                        return true;
                    }
                }
                return false;
            }).collect(Collectors.toList());
            if(papers.isEmpty()) {
                return papers;
            }
        }

        if(refinementsMap.containsKey("publisher")){
            papers=papers.stream().filter((Paper paper)->{
                String publisher=paper.getPublisher();
                for(String v:refinementsMap.get("publisher")){
                    if(publisher.contains(v)){
                        return true;
                    }
                }
                return false;
            }).collect(Collectors.toList());
            if(papers.isEmpty()){
                return papers;
            }
        }
        return papers;
    }

    private Map<String,List<String>> refineAnalyze(List<String> refinements){
        Map<String,List<String>> res=new HashMap<>();
        for(String refinement:refinements){
            String[] r=refinement.split(":");
            if(r.length!=2){
                throw new BadReqException();
            }
            r[0]=r[0].toLowerCase();
            List<String> value=res.getOrDefault(r[0],new ArrayList<>());
            value.add(r[1]);
            res.put(r[0],value);
        }
        return res;
    }


    private <K> List<Pair<K, Integer>> transformHash(Map<K, Integer> hash, final int limit) {
        List<Pair<K, Integer>> ans = new LinkedList<>();
        for (K k : hash.keySet()) {
            ans.add(new Pair<>(k, hash.get(k)));
        }
        ans.sort((o1, o2) -> o2.getSecond() - o1.getSecond());
        ans = ans.subList(0, Math.min(ans.size(), limit));
        return new LinkedList<>(ans);
    }


}
