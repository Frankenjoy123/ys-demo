package com.yunsoo.data.service.service.contract;


import com.yunsoo.data.service.dbmodel.AccountPermissionModel;
import com.yunsoo.data.service.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;

/**
 * Created by KB
 */
public class AccountPermission {
    private Long id;
    private AccountOrg accountOrg;
    private Long permissionGroup1;
    private Long permissionGroup2;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AccountOrg getAccountOrg() { return accountOrg; }
    public void setAccountOrg(AccountOrg accountOrgId) { this.accountOrg = accountOrg; }
    public Long getPermissionGroup1() { return permissionGroup1; }
    public void setPermissionGroup1(long permissionGroup1) { this.permissionGroup1 = permissionGroup1; }
    public Long getPermissionGroup2() { return permissionGroup2; }
    public void setPermissionGroup2(long permissionGroup2) { this.permissionGroup2 = permissionGroup2; }

    public Long[] getPermissionGroups() {
        Long info[] = {permissionGroup1, permissionGroup2};
        return info;
    }

    public static AccountPermissionModel ToModel(AccountPermission ap) {
        if (ap == null) return null;
        AccountPermissionModel model = new AccountPermissionModel();
        BeanUtils.copyProperties(ap, model, SpringBeanUtil.getNullPropertyNames(ap));
        return model;
    }

    public static AccountPermission FromModel(AccountPermissionModel model) {
        if (model == null) return null;
        AccountPermission ap = new AccountPermission();
        BeanUtils.copyProperties(model, ap, SpringBeanUtil.getNullPropertyNames(model));
        return ap;
    }

}
