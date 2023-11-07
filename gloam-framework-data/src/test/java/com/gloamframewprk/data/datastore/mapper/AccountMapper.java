package com.gloamframewprk.data.datastore.mapper;

import com.gloamframewprk.data.datastore.entity.Account;
import com.mybatisflex.annotation.UseDataSource;
import com.mybatisflex.core.BaseMapper;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月07日 12:48
 */
@UseDataSource("p2")
public interface AccountMapper extends BaseMapper<Account> {
}
