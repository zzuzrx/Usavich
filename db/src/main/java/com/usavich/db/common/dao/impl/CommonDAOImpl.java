package com.usavich.db.common.dao.impl;

import com.usavich.db.common.dao.def.CommonDAO;
import com.usavich.entity.common.Experience;
import com.usavich.entity.common.SystemMessage;
import com.usavich.entity.common.VersionControl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: p
 * Date: 13-9-14
 * Time: 下午1:39
 * To change this template use File | Settings | File Templates.
 */
public class CommonDAOImpl implements CommonDAO{

    @Autowired
    private CommonMapper commonMapper;

    @Override
    public Experience getExperienceLevel(double experience) {
        return commonMapper.getExperienceLevel(experience);
    }

    @Override
    public List<SystemMessage> getSystemMessage(Date lastUpdateTime) {
        return commonMapper.getSystemMessageInfo(lastUpdateTime);
    }

    @Override
    public VersionControl getVersionControl(String platform) {
        return commonMapper.getVersionControl(platform);
    }

    @Override
    public void updateVersionControl(VersionControl versionControl) {
        commonMapper.updateVersionControl(versionControl);
    }
}
