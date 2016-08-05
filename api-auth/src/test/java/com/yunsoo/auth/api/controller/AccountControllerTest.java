package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.*;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by:   Lijian
 * Created on:   2016-07-13
 * Descriptions:
 */
public class AccountControllerTest extends TestBase {

    private Account testAccount;

    private String testAccountLoginToken;

    @Before
    public void createAccount() {
        AccountCreationRequest r = new AccountCreationRequest();
        r.setOrgId(YUNSU_ORG_ID);
        r.setIdentifier("testAccount");
        r.setFirstName("Account");
        r.setLastName("UT");
        r.setPhone("123456789");
        r.setPassword("test");
        testAccount = restClient.post("account", r, Account.class);
        testAccountLoginToken = restClient.get("loginToken?account_id={0}", Token.class, testAccount.getId()).getToken();
    }

    private void test_loginWithToken() {
        Token request = new Token();
        request.setToken(testAccountLoginToken);
        AccountLoginResponse response = restClient.post("login/token", request, AccountLoginResponse.class);
        assert response.getAccessToken().getExpiresIn() > 0;
    }

    private void test_loginWithPassword(String password) {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setOrganization("2k0r1l55i2rs5544wz5");
        request.setIdentifier("testAccount");
        request.setPassword(password);
        AccountLoginResponse response = restClient.post("login/password", request, AccountLoginResponse.class);
        assert response.getAccessToken().getExpiresIn() > 0;
    }
    
    @Test
    public void test_getById() {
        //get account
        Account account = restClient.get("account/{id}", Account.class, Constants.SYSTEM_ACCOUNT_ID);
        System.out.println(account.getLastName() + account.getFirstName());
    }

    @Test(expected = NotFoundException.class)
    public void test_getById_404_unknownId() {
        restClient.get("account/{id}", Account.class, "idNotExist");
    }

    @Test
    public void test_getByFilter_withQuery() {
        DateTime startDate = new DateTime(2016, 3, 26, 12, 0);
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", YUNSU_ORG_ID)
                .append("status_code", Constants.AccountStatus.AVAILABLE)
                .append("search_text","UT")
                .append("created_datetime_ge",startDate)
                .append("created_datetime_le", DateTime.now())
                .build();
        List<Account> list = restClient.get("account"+ query, new ParameterizedTypeReference<List<Account>>() {
        });
        assert list.size() >= 1;
    }

    @Test
    public void test_getByFilter_noQuery() {
        List<Account> list = restClient.get("account", new ParameterizedTypeReference<List<Account>>() {
        });
        assert list.size() >= 2;
    }

    @Test(expected = NotFoundException.class)
    public void test_getByFilter_404_unknownId() {
        restClient.get("account/{id}", Account.class, "idNotExist");
    }

    @Test
    public void test_create() {
        Account account = restClient.get("account/{id}", Account.class, testAccount.getId());
        assertEquals(account.getFirstName(), testAccount.getFirstName());
        assertEquals(account.getLastName(), testAccount.getLastName());
    }
    
    @Test
    public void test_batch_create() {
        //async create 100 accounts
        IntStream.range(0, 100).parallel().mapToObj(i -> {
            AccountCreationRequest r = new AccountCreationRequest();
            r.setOrgId(YUNSU_ORG_ID);
            r.setIdentifier("test" + i);
            r.setFirstName("Account" + i);
            r.setLastName("UT");
            r.setPhone("123456789");
            r.setPassword("test");
            return restClient.postAsync("account", r, Account.class);
        }).map(f -> {
            try {
                Account a = f.get();
                System.out.println(a.getId() + " " + a.getFirstName());
                return a;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }
    
    @Test
    public void test_count() {
        long count = restClient.get("account/count?org_id={id}", Long.class, YUNSU_ORG_ID);
        assert count > 0;
    }

    @Test
    public void test_count_notExistingOrg() {
        long count = restClient.get("account/count?org_id={id}", Long.class, "12345Wrong");
        assert count == 0;
    }

    @Test
    public void test_count_emptyOrg() {
        long count = restClient.get("account/count?org_id={id}", Long.class, "");
        assert count == 0;
    }

    @Test
    public void test_count_noParameterSet() {
        long count = restClient.get("account/count", Long.class);
        assert count > 0;
    }

    @Test
    public void test_count_status() {
        List<String> codes = Arrays.asList(Constants.AccountStatus.AVAILABLE,"a");
        long count = restClient.get("account/count?" + new QueryStringBuilder().append("status_code_in", codes).build(), Long.class);
        assert count > 0;
    }

    @Test
    public void test_patchUpdateAccount_200() {
        String id = testAccount.getId();
        Account account = restClient.get("account/{id}", Account.class, id);
        account.setFirstName("great");
        restClient.patch("account/{id}", account, id);
        account = restClient.get("account/{id}", Account.class, id);
        assert account.getFirstName().equals("great");
    }

    @Test(expected = UnauthorizedException.class)
    public void test_patchUpdateSystemAccount_401() {
        String id = Constants.SYSTEM_ACCOUNT_ID;
        Account account = restClient.get("account/{id}", Account.class, id);
        account.setFirstName("great");
        restClient.patch("account/{id}", account, id);
    }

    @Test(expected = BadRequestException.class)
    public void test_patchUpdateAccount_400_nullAccount() {
        restClient.patch("account/{id}", null, Constants.SYSTEM_ACCOUNT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void test_patchUpdateAccount_404_wrongId() {
        Account account = new Account();
        account.setFirstName("myName");
        restClient.patch("account/{id}", account, "NotExistedId");
    }

    @Test(expected = UnauthorizedException.class)
    public void test_disableAccount_200() {
        test_loginWithToken();
        String url = String.format("account/%s/disable",testAccount.getId());
        restClient.post(url, null, null);
        test_loginWithToken();
    }

    @Test
    public void test_enableAccount_200() {
        String url = String.format("account/%s/disable",testAccount.getId());
        restClient.post(url, null, null);
        url = String.format("account/%s/enable",testAccount.getId());
        restClient.post(url, null, null);
        test_loginWithToken();
    }

    /*
    @Test
    public void test_updatePassword_200() {
        AccountUpdatePasswordRequest request = new AccountUpdatePasswordRequest();
        request.setOldPassword("x");
        request.setNewPassword("new_password");
        restClient.post("account/current/password", request, null);
    }

    @Test
    public void test_updatePassword_wrongOldPassword_404() {
        AccountUpdatePasswordRequest request = new AccountUpdatePasswordRequest();
        request.setOldPassword("xxx");
        request.setNewPassword("new_password");
        restClient.post("account/current/password", request, null);
    }
    */

    @Test(expected = UnauthorizedException.class)
    public void test_changePassword_200() {
        test_loginWithPassword("test");
        restClient.put("account/{0}/password", "newPassword" , testAccount.getId());
        test_loginWithPassword("newPassword");
        test_loginWithPassword("");
    }

    @Test(expected = BadRequestException.class)
    public void test_updatePassword_400_emptyRequest() {
        AccountUpdatePasswordRequest request = new AccountUpdatePasswordRequest();
        restClient.post("login/password", request, null);
    }

    @Test(expected = BadRequestException.class)
    public void test_updatePassword_400_nullRequest() {
        restClient.post("login/password", null, null);
    }

    @Test
    public void test_getAllPermissionByAccountId() {
        List<PermissionEntry> list = restClient.get("account/{id}/permission", new ParameterizedTypeReference<List<PermissionEntry>>() {
        }, Constants.SYSTEM_ACCOUNT_ID);
        System.out.println("Permission are "+list);
    }

    @Test
    public void test_getAccountLoginLogByAccountId() {
        List<AccountLoginLog> list = restClient.get("account/{account_id}/loginLog", new ParameterizedTypeReference<List<AccountLoginLog>>() {
        }, Constants.SYSTEM_ACCOUNT_ID);
        System.out.println("Account Login Log are "+list);
    }
}
