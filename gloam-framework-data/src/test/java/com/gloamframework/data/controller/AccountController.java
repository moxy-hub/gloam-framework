package com.gloamframework.data.controller;

import com.gloamframework.data.datastore.entity.Account;
import com.gloamframework.data.datastore.mapper.AccountMasterMapper;
import com.gloamframework.data.service.AccountService;
import com.gloamframework.web.response.WebList;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月07日 14:34
 */
@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMasterMapper accountMasterMapper;

    @GetMapping("/account")
    public WebList<Account> lisAccount() {
        return WebList.success(accountService.testAccount());
    }

    @GetMapping("/account1")
    public WebList<Account> lisAccount1() {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select();
        return WebList.success(accountMasterMapper.selectListByQuery(queryWrapper));
    }

}
