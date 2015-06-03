package com.yunsoo.data.api.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.DeviceEntity;
import com.yunsoo.data.service.repository.DeviceRepository;
import com.yunsoo.data.service.service.contract.Device;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Jerry on 3/13/2015.
 * Updated by Zhe on 5/11/2015
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Device getDeviceById(@PathVariable(value = "id") String id) {
        if (id.isEmpty()) throw new BadRequestException("id不能为空！");
        Device device = Device.FromEntity(deviceRepository.findById(id));
        return device;
    }

    @RequestMapping(value = "/org/{id}", method = RequestMethod.GET)
    public List<Device> getDeviceByOrgId(@PathVariable(value = "id") String id,
                                         @RequestParam(value = "index") Integer index,
                                         @RequestParam(value = "size") Integer size) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<Device> deviceList = Device.FromEntityList(deviceRepository.findByOrgId(id, new PageRequest(index, size)));
        if (deviceList == null || deviceList.size() < 1) {
            throw new NotFoundException(40401, "找不到组织的device记录! 组织ID = " + id);
        }
        return deviceList;
    }

    @RequestMapping(value = "/whose/{id}", method = RequestMethod.GET)
    public List<Device> getDeviceByAccountId(@PathVariable(value = "id") String id,
                                             @RequestParam(value = "index") Integer index,
                                             @RequestParam(value = "size") Integer size) {
        if (id == null || id.isEmpty()) throw new BadRequestException("id不能为空！");
        if (index == null || index < 0) throw new BadRequestException("Index必须为不小于0的值！");
        if (size == null || size < 0) throw new BadRequestException("Size必须为不小于0的值！");

        List<Device> deviceList = Device.FromEntityList(deviceRepository.findByCreatedAccountId(id, new PageRequest(index, size)));
        if (deviceList == null || deviceList.size() < 1) {
            throw new NotFoundException(40401, "找不到Account的device记录! AccountID = " + id);
        }
        return deviceList;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String createDevice(@RequestBody Device device) {
        device.setCreatedDateTime(DateTime.now());
        device.setModifiedDatetime(DateTime.now());
        DeviceEntity entity = deviceRepository.save(Device.ToEntity(device));
        return entity.getId();
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void updateDevice(@RequestBody Device device) {
        device.setModifiedDatetime(DateTime.now());
        DeviceEntity entity = deviceRepository.save(Device.ToEntity(device));
    }

}
