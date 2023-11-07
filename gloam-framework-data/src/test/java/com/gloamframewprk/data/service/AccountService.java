package com.gloamframewprk.data.service;

import com.gloamframewprk.data.datastore.entity.Account;
import com.gloamframewprk.data.datastore.mapper.AccountMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月07日 12:49
 */
@Service
public class AccountService {

    @Autowired
    private AccountMapper accountMapper;

    public List<Account> testAccount() {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select();
        return accountMapper.selectListByQuery(queryWrapper);
    }

}
