package com.gloam.mybatis.flex.datastore.mapper;


import com.gloam.mybatis.flex.datastore.entity.Account;
import com.mybatisflex.annotation.UseDataSource;
import com.mybatisflex.core.BaseMapper;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月07日 16:22
 */
@UseDataSource("master")
public interface AccountMasterMapper extends BaseMapper<Account> {
}
