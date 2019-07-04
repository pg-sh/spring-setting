package com.son.mvcboard.article.controller;

import com.son.mvcboard.article.domain.ArticleVO;
import com.son.mvcboard.article.service.ArticleService;
import com.son.mvcboard.commons.paging.Criteria;
import com.son.mvcboard.commons.paging.PageMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;


@Controller
@RequestMapping("/article")
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleService articleService;

    @Inject
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // 등록 페이지 이동
    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public String writeGET() {

        logger.info("write GET...");

        return "/article/write";
    }

    // 등록 처리
    @RequestMapping(value = "write", method = RequestMethod.POST)
    public String writePOST(ArticleVO articleVO,
                            RedirectAttributes redirectAttributes) throws Exception {

        logger.info("write POST");
        logger.info(articleVO.toString());

        articleService.create(articleVO);
        redirectAttributes.addFlashAttribute("msg", "regSuccess");

        return "redirect:/article/listPaging";
    }

    // 목록 페이지 이동
    @RequestMapping(value = "/listPaging", method = RequestMethod.GET)
    public String listPaging(Model model, Criteria criteria) throws Exception {
        logger.info("listPaging");

        PageMaker pageMaker = new PageMaker();
        pageMaker.setCriteria(criteria);

        pageMaker.setTotalCount(articleService.countArticles(criteria));

        model.addAttribute("articles", articleService.listCriteria(criteria));
        model.addAttribute("pageMaker", pageMaker);

        return "/article/list_paging";
    }

    // 조회 페이지 이동
    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public String read(@RequestParam("articleNo") int articleNo,
                       Model model) throws Exception {

        logger.info("read ...");
        model.addAttribute("article", articleService.read(articleNo));

        return "/article/read";
    }

    // 수정 페이지 이동
    @RequestMapping(value = "/modify", method = RequestMethod.GET)
    public String modifyGET(@RequestParam("articleNo") int articleNo,
                            Model model) throws Exception {

        logger.info("modifyGet ...");
        model.addAttribute("article", articleService.read(articleNo));

        return "/article/modify";
    }

    // 수정 처리
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public String modifyPOST(ArticleVO articleVO,
                             RedirectAttributes redirectAttributes) throws Exception {

        logger.info("modifyPOST ...");
        articleService.update(articleVO);
        redirectAttributes.addFlashAttribute("msg", "modSuccess");

        return "redirect:/article/listPaging";
    }

    // 삭제 처리
    @RequestMapping(value ="/remove", method = RequestMethod.POST)
    public String remove(@RequestParam("articleNo") int articleNo,
                         RedirectAttributes redirectAttributes) throws Exception {

        logger.info("remove ...");
        articleService.delete(articleNo);
        redirectAttributes.addFlashAttribute("msg", "delSuccess");

        return "redirect:/article/listPaging";
    }


}