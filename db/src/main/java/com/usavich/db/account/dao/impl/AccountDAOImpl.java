package com.usavich.db.account.dao.impl;

import com.usavich.db.account.dao.def.AccountDAO;
import com.usavich.entity.enums.FriendStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.usavich.entity.account.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: LeonLu
 * Date: 3/5/13
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountDAOImpl implements AccountDAO {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public UserInfo getAccountInfo(String userEmail, String password) {
        return accountMapper.getAccountInfo(userEmail, password);
    }

    @Override
    public UserInfo getAccountInfoByMail(String userEmail) {
        return accountMapper.getAccountInfoByMail(userEmail);
    }

    @Override
    public UserInfo getAccountInfoByID(Integer userId) {
        return accountMapper.getAccountInfoByID(userId);
    }

    @Override
    public UserInfo updateAccountInfo(UserInfo userInfo) {
        return accountMapper.updateAccountInfo(userInfo);
    }

    @Override
    public void updateAccountBase(UserBase userBase) {
        accountMapper.updateAccountBase(userBase);
    }

    @Override
    public UserInfo createAccountInfo(UserBase userBase) {
        accountMapper.createBase(userBase);
        UserInfo accountInfo = new UserInfo(userBase);
        accountMapper.createDetail(accountInfo);
        UserLocation userLocation = new UserLocation();
        userLocation.setUserId(userBase.getUserId());
        createUserLocation(userLocation);
        return accountInfo;
    }

    @Override
    public List<UserFriend> getUserFriends(Integer userId) {
        return accountMapper.getUserFriends(userId);
    }

    @Override
    public UserFriend getUserFriend(Integer userId, Integer friendId) {
        return accountMapper.getUserFriend(userId, friendId);
    }

    @Override
    public void createUserFriendInvite(UserFriend userFriend) {
        userFriend.setFriendStatus(FriendStatus.Invited.ordinal());
        accountMapper.createUserFriend(userFriend);
        UserFriend userFriendInvited = new UserFriend();
        userFriendInvited.setUserId(userFriend.getFriendId());
        userFriendInvited.setFriendId(userFriend.getUserId());
        userFriendInvited.setFriendStatus(FriendStatus.NeedAccept.ordinal());
        userFriendInvited.setAddTime(userFriend.getAddTime());
        accountMapper.createUserFriend(userFriendInvited);
    }

    @Override
    public void updateUserFriendStatus(UserFriend userFriend) {
        accountMapper.updateUserFriend(userFriend);
        UserFriend userFriendStatus = new UserFriend();
        userFriendStatus.setUserId(userFriend.getFriendId());
        userFriendStatus.setFriendId(userFriend.getUserId());
        userFriendStatus.setFriendStatus(userFriend.getFriendStatus());
        userFriendStatus.setAddTime(userFriend.getAddTime());
        accountMapper.updateUserFriend(userFriendStatus);
    }

    @Override
    public UserLocation getUserLocation(Integer userId) {
        UserLocation ul = accountMapper.getUserLocation(userId);
        return  ul;
    }

    @Override
    public void createUserLocation(UserLocation userLocation) {
        accountMapper.createUserLocation(userLocation);
    }

    @Override
    public void updateUserLocation(UserLocation userLocation) {
        accountMapper.updateUserLocation(userLocation);
    }

    @Override
    public List<UserLocation> getUserLocations() {
        return accountMapper.getUserLocations();
    }
}
