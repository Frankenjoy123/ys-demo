package com.yunsoo.data.api.dto;

import com.yunsoo.data.service.service.contract.AccountPermission;
import com.yunsoo.data.service.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;

/**
 * Created by KB on 2015/3/3.
 */
public class AccountPermissionDto {
    private long id;
    private long accountOrgId;
    private long permissionGroup1;
    private long permissionGroup2;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getAccountOrgId() { return accountOrgId; }
    public void setAccountOrgId(long accountOrgId) { this.accountOrgId = accountOrgId; }
    public long getPermissionGroup1() { return permissionGroup1; }
    public void setPermissionGroup1(long permissionGroup1) { this.permissionGroup1 = permissionGroup1; }
    public long getPermissionGroup2() { return permissionGroup2; }
    public void setPermissionGroup2(long permissionGroup2) { this.permissionGroup2 = permissionGroup2; }

    public AccountPermission ToAccountPermission(AccountPermissionDto dto) {
        if (dto == null) return null;
        AccountPermission ap = new AccountPermission();
        BeanUtils.copyProperties(dto, ap, SpringBeanUtil.getNullPropertyNames(dto));
        return ap;
    }

    public AccountPermissionDto FromAccountPermission(AccountPermission ap) {
        if (ap == null) return null;
        AccountPermissionDto dto = new AccountPermissionDto();
        BeanUtils.copyProperties(ap, dto, SpringBeanUtil.getNullPropertyNames(ap));
        return dto;
    }
}
