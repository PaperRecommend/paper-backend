package com.paper;

import com.paper.dao.PaperRepository;
import com.paper.entity.Paper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataESPaperRepositoryTest {

    @Autowired
    private PaperRepository paperRepository;

    //根据 id 查询
    @Test
    public void findById(){

        Paper paper= paperRepository.findById(13407L).get();
        System.out.println(paper.getTitle());

        System.out.println(paper.getAuthors()[0].getName());
        System.out.println(paper.getYear());
        System.out.println(paper.getN_citation());
        System.out.println(paper.getDoi());
        System.out.println(paper.getFos()[0].getName());
        System.out.println(paper.getPage_start());
        System.out.println(paper.getPage_end());
        System.out.println(paper.getDoc_type());
        System.out.println(paper.getReferences()[0]);
        System.out.println(paper.getFos()[0].getName());
        System.out.println(paper.getVenue().getRaw());

        System.out.println(paper.getAbstract_());
    }


}

