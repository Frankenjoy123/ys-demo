package com.yunsoo.auth.service;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.api.security.AuthAccount;
import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.api.util.PageUtils;
import com.yunsoo.auth.dao.entity.DeviceEntity;
import com.yunsoo.auth.dao.repository.DeviceRepository;
import com.yunsoo.auth.dto.Device;
import com.yunsoo.common.web.client.Page;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Created by:   Lijian
 * Created on:   2016-07-25
 * Descriptions:
 */
@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private AccountTokenService accountTokenService;


    public Device getById(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return null;
        }
        return toDevice(deviceRepository.findOne(deviceId));
    }

    public Page<Device> getByFilter(String orgId, String authAccountId, Pageable pageable) {
        return PageUtils.convert(
                !StringUtils.isEmpty(authAccountId) ? deviceRepository.findByAuthAccountId(authAccountId, pageable)
                        : !StringUtils.isEmpty(orgId) ? deviceRepository.findByOrgId(orgId, pageable)
                        : deviceRepository.findAll(pageable)).map(this::toDevice);
    }

    public Device register(Device device) {
        Assert.notNull(device, "device must not be null");
        Assert.hasText(device.getId(), "device id must not be null or empty");

        AuthAccount currentAccount = AuthUtils.getCurrentAccount();
        String accountId = currentAccount.getId();
        String orgId = currentAccount.getOrgId();

        DeviceEntity entity = deviceRepository.findOne(device.getId());
        if (entity == null) {
            entity = new DeviceEntity();
            entity.setId(device.getId());
            entity.setCreatedAccountId(accountId);
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setAppId(device.getAppId());
        entity.setOrgId(orgId);
        entity.setAuthAccountId(accountId);
        entity.setStatusCode(Constants.DeviceStatus.AVAILABLE);
        entity.setName(device.getName());
        entity.setOs(device.getOs());
        entity.setComments(device.getComments());
        entity.setModifiedAccountId(accountId);
        entity.setModifiedDatetime(DateTime.now());

        entity = deviceRepository.save(entity);
        return toDevice(entity);
    }

    public void unregister(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return;
        }
        deviceRepository.delete(deviceId);
        accountTokenService.expirePermanentTokenByDeviceId(deviceId);
    }

    private Device toDevice(DeviceEntity entity) {
        if (entity == null) {
            return null;
        }
        Device device = new Device();
        device.setId(entity.getId());
        device.setAppId(entity.getAppId());
        device.setOrgId(entity.getOrgId());
        device.setAuthAccountId(entity.getAuthAccountId());
        device.setStatusCode(entity.getStatusCode());
        device.setName(entity.getName());
        device.setOs(entity.getOs());
        device.setComments(entity.getComments());
        device.setCreatedAccountId(entity.getCreatedAccountId());
        device.setCreatedDateTime(entity.getCreatedDateTime());
        device.setModifiedAccountId(entity.getModifiedAccountId());
        device.setModifiedDatetime(entity.getModifiedDatetime());
        return device;
    }
}
