package com.example.demo;


import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.repositories.MyDataRepository;

@Controller
public class HelloController {
	@Autowired
	MyDataRepository repository;

	@PersistenceContext
	EntityManager entityManager;

	MyDataDaoImpl dao;
	/*
	 * 初期表示用メソッド
	 */
	@PostConstruct
	public void init() {
		dao = new MyDataDaoImpl(entityManager);
		for(int i =10; i < 13; i++) {
			MyData data = new MyData();
			data.setName("takashi" + i);
			data.setAge(i);
			data.setMail("takashi" + i + "@gmail.com");
			data.setMemo("090-1234-124" + i);
			repository.saveAndFlush(data);
		}
	}
	/*
	 * ホーム表示用メソッド
	 */
	@RequestMapping(value = "/" ,method = RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("msg","sample" );
		Iterable<MyData> list = dao.getAll();
		mav.addObject("datalist",list);
		return mav;
	}
	/*
	 *
	 */
	@RequestMapping(value = "/find" ,method = RequestMethod.GET)
	public ModelAndView find(ModelAndView mav) {
		mav.setViewName("find");
		mav.addObject("title","find" );
		mav.addObject("msg","input to search" );
		mav.addObject("value","" );
		Iterable<MyData> list = dao.getAll();
		mav.addObject("datalist",list);
		return mav;
	}
	/*
	 *
	 */
	@RequestMapping(value = "/find" ,method = RequestMethod.POST)
	public ModelAndView search(HttpServletRequest request, ModelAndView mav) {
		mav.setViewName("find");
		String param = request.getParameter("fstr");
		if(param == "") {
			mav = new ModelAndView("redirect:/");
		}else {
			mav.addObject("title","find result" );
			mav.addObject("msg","[" + param + "]nokensakukekka" );
			mav.addObject("value",param );
			@SuppressWarnings("unused")
			List <MyData> list = dao.find(param);
		}
		return mav;
	}
	/*
	 * 登録用メソッド
	 */
	@RequestMapping(value = "/" ,method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView form(@ModelAttribute("formModel") @Validated MyData mydata,
			BindingResult result, ModelAndView mav) {
		ModelAndView res = null;
		if(!result.hasErrors()) {
			repository.saveAndFlush(mydata);
			res = new ModelAndView("redirect:/");
		}else {
			mav.setViewName("index");
			mav.addObject("msg", "sorry!!error has been occured!");
			Iterable<MyData> list = repository.findAll();
			mav.addObject("datalist",list);
			res = mav;
		}
		return res;
	}

	/*
	 * 更新ページ遷移用メソッド
	 */
	@RequestMapping(value = "/edit/{id}" ,method = RequestMethod.GET)
	public ModelAndView edit(@ModelAttribute("formModel") MyData mydata,
			@PathVariable int id,ModelAndView mav) {
		mav.setViewName("edit");
		mav.addObject("title","edit data" );
		Optional <MyData> data = repository.findById((long)id);
		mav.addObject("formModel",data.get());
		return mav;
	}

	/*
	 * 更新実行用メソッド
	 */
	@RequestMapping(value = "/edit" ,method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView update(@ModelAttribute("formModel") MyData mydata, ModelAndView mav) {
		repository.saveAndFlush(mydata);
		return new ModelAndView("redirect:/");
	}

	/*
	 * 削除ページ遷移用メソッド
	 */
	@RequestMapping(value = "/delete/{id}" ,method = RequestMethod.GET)
	public ModelAndView delete(@ModelAttribute("formModel") MyData mydata,
			@PathVariable int id,ModelAndView mav) {
		mav.setViewName("delete");
		mav.addObject("title","delete data" );
		Optional <MyData> data = repository.findById((long)id);
		mav.addObject("formModel",data.get());
		return mav;
	}

	/*
	 * 削除実行メソッド
	 */
	@RequestMapping(value = "/delete" ,method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView remove(@RequestParam long id, ModelAndView mav) {
		repository.deleteById(id);
		return new ModelAndView("redirect:/");
	}
}