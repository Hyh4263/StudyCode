package com.example.medicine.controller;

import com.example.medicine.entity.History;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 历史控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping("history")
public class HistoryController extends BaseController<History> {


}
