package com.yunsoo.service.contract;


import com.yunsoo.dbmodel.AccountPermissionModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

/**
 * Created by KB
 */
public class AccountPermission {
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

    public AccountPermissionModel ToModel(AccountPermission ap) {
        if (ap == null) return null;
        AccountPermissionModel model = new AccountPermissionModel();
        BeanUtils.copyProperties(ap, model);
        return model;
    }

    public AccountPermission FromModel(AccountPermissionModel model) {
        if (model == null) return null;
        AccountPermission ap = new AccountPermission();
        BeanUtils.copyProperties(model, ap);
        return ap;
    }

}
