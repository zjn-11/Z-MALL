package com.zjn.mall.service;

import com.zjn.mall.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
    /**
 * @ClassName SysUserService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月05日 00:46:00
 */

public interface SysUserService extends IService<SysUser>{

    /**
     * 新增管理员
     *
     * @param sysUser
     * @return
     */
    Integer saveSysUser(SysUser sysUser);
}
