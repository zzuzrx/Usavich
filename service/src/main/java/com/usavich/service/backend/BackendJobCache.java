package com.usavich.service.backend;

import com.usavich.common.lib.CommonUtils;
import com.usavich.entity.common.SystemMessage;
import com.usavich.entity.common.VersionControl;
import com.usavich.entity.mission.Mission;
import com.usavich.service.common.def.CommonService;
import com.usavich.service.mission.def.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: p
 * Date: 13-9-3
 * Time: 上午11:03
 * To change this template use File | Settings | File Templates.
 */
@Service
public class BackendJobCache {

    @Autowired
    private MissionService missionService;

    @Autowired
    private CommonService commonService;

    public static VersionControl versionControlIOS = new VersionControl();

    public static List<Mission> allMissions = new ArrayList<Mission>();

    public static List<SystemMessage> allMessages = new ArrayList<SystemMessage>();

    public static Date missionLastTime = CommonUtils.parseDateDefaultToNull("2001-01-01 00:00:00");

    public static Date missionFirstTime = CommonUtils.parseDateDefaultToNull("3001-01-01 00:00:00");

    public static Date messageLastTime = CommonUtils.parseDateDefaultToNull("2001-01-01 00:00:00");

    public static Date messageFirstTime = CommonUtils.parseDateDefaultToNull("3001-01-01 00:00:00");

    public void missionServiceJob() {
        allMissions = missionService.getMissions(null, CommonUtils.parseDateDefaultToNull("2001-01-01 00:00:00"), -1);
        for (Mission mission : allMissions) {
            if (mission.getLastUpdateTime().after(missionLastTime)) {
                missionLastTime = mission.getLastUpdateTime();
            }
            if (mission.getLastUpdateTime().before(missionFirstTime)) {
                missionFirstTime = mission.getLastUpdateTime();
            }
        }
    }


    public void systemMessageServiceJob() {
        allMessages = commonService.getSystemMessage(CommonUtils.parseDateDefaultToNull("2001-01-01 00:00:00"));
        for (SystemMessage systemMessage : allMessages) {
            if (systemMessage.getLastUpdateTime().after(messageLastTime)) {
                messageLastTime = systemMessage.getLastUpdateTime();
            }
            if (systemMessage.getLastUpdateTime().before(messageFirstTime)) {
                messageFirstTime = systemMessage.getLastUpdateTime();
            }
        }
    }

    public void versionServiceJob() {
        versionControlIOS = commonService.getVersionControl("ios");
    }
}