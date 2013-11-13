package com.usavich.service.plan.impl;

import com.usavich.db.common.dao.def.CommonDAO;
import com.usavich.db.plan.dao.def.PlanDAO;
import com.usavich.entity.common.IDGeneration;
import com.usavich.entity.mission.Mission;
import com.usavich.entity.plan.Plan;
import com.usavich.entity.plan.PlanCollect;
import com.usavich.entity.plan.PlanRunHistory;
import com.usavich.entity.plan.PlanUserFollow;
import com.usavich.service.backend.BackendJobCache;
import com.usavich.service.mission.def.MissionService;
import com.usavich.service.plan.def.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: p
 * Date: 13-11-12
 * Time: 上午9:42
 * To change this template use File | Settings | File Templates.
 */
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanDAO planDAO;

    @Autowired
    private CommonDAO commonDAODAO;

    @Autowired
    private MissionService missionService;

    @Override
    public List<Plan> getPlansForRest(Integer pageNo) {
        if (BackendJobCache.first100Plan.size() == 0) {
            getPlanByPageNo(0, 100);
        }
        List<Plan> returnedPlan = new ArrayList<Plan>();
        Integer from = pageNo == null ? 0 : pageNo * 10;
        if (from < BackendJobCache.first100Plan.size()) {
            for (int i = from; i < 10; i++) {
                if (BackendJobCache.first100Plan.size() > i) {
                    returnedPlan.add(BackendJobCache.first100Plan.get(i));
                }
            }
        }
        return returnedPlan;
    }

    @Override
    public List<Plan> getPlanByPageNo(Integer pageNo, Integer pageSize) {
        Integer from = pageNo == null ? 0 : pageNo * 10;
        return planDAO.getPlansByPage(from, pageSize);
    }

    @Override
    public Plan getPlan(Integer planId, Date lastUpdateTime, Integer needMissions) {
        Plan plan = planDAO.getPlan(planId, lastUpdateTime);
        if (needMissions == 1) {
            plan.setMissions(missionService.getMissionsByPlanId(planId));
        }
        return plan;
    }

    @Override
    public List<PlanCollect> getPlanCollection(Integer userId, Date lastUpdateTime) {
        return planDAO.getPlanCollection(userId, lastUpdateTime);
    }

    @Override
    @Transactional
    public void updateUserCollects(Integer userId, List<PlanCollect> planCollects) {
        for (int i = 0; i < planCollects.size(); i++) {
            PlanCollect planCollect = planCollects.get(i);
            planCollect.setCollectTime(new Date());
            planDAO.createUserCollect(userId, planCollects.get(i));
        }
    }

    @Override
    public List<PlanRunHistory> getPlanRunHistory(Integer userId, Date lastUpdateTime) {
        return planDAO.getPlanRunHistory(userId, lastUpdateTime);
    }

    @Override
    @Transactional
    public PlanRunHistory checkRunningHistoryStatus(Integer userId, PlanRunHistory planHistory) {
        PlanRunHistory currentHistory = planDAO.getPlanRunning(userId);
        if (planHistory == null || planHistory.getPlanId() == null) {
            return currentHistory;
        }
        if (planHistory.getRemainingMissions() == 0) {
            planHistory.setHistoryStatus(1);
            planHistory.setEndTime(new Date());
            planHistory.setNextMissionId(null);
        }
        if (planHistory.getHistoryStatus() == -1) {
            planHistory.setEndTime(new Date());
        }
        if (currentHistory == null || currentHistory.getPlanRunUuid() == null) {
            planDAO.createPlanRunning(userId, planHistory);
        } else if (currentHistory.getRemainingMissions() >= planHistory.getRemainingMissions()) {
            planDAO.updatePlanRunning(userId, planHistory);
        } else {
            return currentHistory;
        }
        return planHistory;
    }

    @Override
    public List<PlanRunHistory> getPlanRunningByPlanId(Integer planId, Integer pageNo) {
        Integer from = pageNo == null ? 0 : pageNo * 10;
        Integer pageSize = 30;
        return planDAO.getPlanRunningByPlanId(planId, from, pageSize);
    }

    @Override
    public List<PlanRunHistory> getPlanRunningByUserId(Integer userId, Integer pageNo) {
        Integer from = pageNo == null ? 0 : pageNo * 10;
        Integer pageSize = 30;
        return planDAO.getPlanRunningByUserId(userId, from, pageSize);
    }

    @Override
    public List<PlanUserFollow> getPlanFollower(Integer userId, Date lastUpdateTime) {
        return planDAO.getPlanFollower(userId, lastUpdateTime);
    }

    @Override
    @Transactional
    public void updatePlanFollower(Integer userId, List<PlanUserFollow> planFollow) {
        for (int i = 0; i < planFollow.size(); i++) {
            PlanUserFollow planUserFollow = planFollow.get(i);
            planUserFollow.setAddTime(new Date());
            planDAO.createPlanFollower(userId, planFollow.get(i));
        }
    }

    @Override
    @Transactional
    public Plan createPlan(Integer userId, Plan newPlan) {
        IDGeneration idGeneration = commonDAODAO.getIDGenerationInfo();
        if (newPlan != null && newPlan.getTotalMissions() != null && newPlan.getTotalMissions() > 0) {
            idGeneration.setPlanId(idGeneration.getPlanId() + 1);
            newPlan.setPlanId(idGeneration.getPlanId());
            newPlan.setLastUpdateTime(new Date());
            String missionIds = "";
            for (Mission mission : newPlan.getMissions()) {
                if (mission.getSequence() != null) {
                    mission.setPlanId(idGeneration.getPlanId());
                    idGeneration.setMissionId(idGeneration.getMissionId() + 1);
                    mission.setMissionId(idGeneration.getMissionId());
                    //todo:: add experience and score calc
                    missionIds = missionIds + idGeneration.getMissionId().toString() + ",";
                    mission.setLastUpdateTime(new Date());
                }
            }
            if (missionIds.length() > 0) {
                missionIds.substring(0, missionIds.length() - 1);
                newPlan.setMissionIds(missionIds);
            }
            planDAO.createPlan(newPlan);
            for (Mission mission : newPlan.getMissions()) {
                if (mission.getSequence() != null) {
                    missionService.createMission(mission);
                }
            }
            commonDAODAO.updateIDGenerationFriend(idGeneration);
            return newPlan;
        }
        return null;
    }
}


