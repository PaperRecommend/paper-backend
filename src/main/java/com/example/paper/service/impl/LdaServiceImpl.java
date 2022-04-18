package com.example.paper.service.impl;

import com.example.paper.dao.LdaRecommend;
import com.example.paper.entity.ldarecommendEntity.LdaUserPaper;
import com.example.paper.service.LdaService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class LdaServiceImpl implements LdaService {

    @Autowired
    LdaRecommend ldaRecommend;


    @Override
    public List<Long> getUserLdaPapers(Integer uid, int size) {

        List<Long> listRandom = new ArrayList<>();
        List<Long> list = new ArrayList<>();
        Optional<LdaUserPaper> opt = ldaRecommend.findById(uid);
        if (opt.isPresent()) {
            LdaUserPaper ldaUserPaper = opt.get();
            Long[] paperIds = ldaUserPaper.getPapers();
            for (Long id : paperIds) {
                list.add(id);
            }
            if (size >= paperIds.length) {
                return list;
            } else {
                for (int i = size; i >= 1; i--) {
                    Random random = new Random();
                    Math.random();
                    //在数组大小之间产生一个随机数 j
                    int j = random.nextInt(list.size() - 1);
                    //取得list 中下标为j 的数据存储到 listRandom 中
                    listRandom.add(list.get(j));
                    //把已取到的数据移除,避免下次再次取到出现重复
                    list.remove(j);
                }
                return listRandom;
            }
        }

        return listRandom;
    }
}
