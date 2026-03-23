package com.yupi.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.usercenter.common.BaseResponse;
import com.yupi.usercenter.common.DeleteRequest;
import com.yupi.usercenter.common.ErrorCode;
import com.yupi.usercenter.common.ResultUtils;
import com.yupi.usercenter.exception.BusinessException;
import com.yupi.usercenter.model.domain.Team;
import com.yupi.usercenter.model.domain.User;
import com.yupi.usercenter.model.domain.UserTeam;
import com.yupi.usercenter.model.dto.TeamQuery;
import com.yupi.usercenter.model.request.TeamAddRequest;
import com.yupi.usercenter.model.request.TeamJoinRequest;
import com.yupi.usercenter.model.request.TeamQuitRequest;
import com.yupi.usercenter.model.request.TeamUpdateRequest;
import com.yupi.usercenter.model.vo.TeamUserVO;
import com.yupi.usercenter.service.TeamService;
import com.yupi.usercenter.service.UserService;
import com.yupi.usercenter.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 队伍接口
 *
 */
@RestController
@RequestMapping("/team")
@Slf4j
public class TeamController {


    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;
    @Resource
    private UserTeamService userTeamService;

    @PostMapping("/add")
    public BaseResponse<Long> createTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        try {
            BeanUtils.copyProperties(team, teamAddRequest);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "属性拷贝失败");
        }
        long teamId = teamService.addTeam(team, loginUser);

        return ResultUtils.success(teamId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean remove = teamService.deleteTeam(deleteRequest.getId(), loginUser);
        if (!remove) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean update = teamService.updateTeam(teamUpdateRequest, loginUser);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeam(@RequestParam Long id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(team);
    }

    //    @GetMapping("/list")
//    public BaseResponse<List<Team>> listTeam(TeamQuery teamQuery, HttpServletRequest request) {
//        if (teamQuery == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        Team team = new Team();
//        try {
//            BeanUtils.copyProperties(team, teamQuery);
//        } catch (Exception e) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "属性拷贝失败");
//        }
//        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
//        List<Team> list = teamService.list(queryWrapper);
//        return ResultUtils.success(list);
//    }
    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> listTeam(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVO> list = teamService.listTeam(teamQuery, isAdmin);
        // 判断用户是否加入队伍
        List<Long> teamIdList = list.stream().map(TeamUserVO::getId).collect(Collectors.toList());
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        try {
            User loginUser = userService.getLoginUser(request);
            queryWrapper.eq("user_id", loginUser.getId());
            queryWrapper.in("team_id", teamIdList);
            List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
            Set<Long> isJoinTeamSet = userTeamList.stream().map(UserTeam::getId).collect(Collectors.toSet());
            list.forEach(teamUserVO -> {
                teamUserVO.setIsJoin(isJoinTeamSet.contains(teamUserVO.getId()));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResultUtils.success(list);
    }

    //todo 分页查询队伍列表
    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> pageTeam(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        try {
            BeanUtils.copyProperties(team, teamQuery);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "属性拷贝失败");
        }
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> page = teamService.page(new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize()), queryWrapper);
        return ResultUtils.success(page);
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean join = teamService.joinTeam(teamJoinRequest, loginUser);
        if (!join) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "加入失败");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean quit = teamService.quitTeam(teamQuitRequest, loginUser);
        if (!quit) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "退出失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 获取用户创建的队伍
     * @param teamQuery
     * @param request
     * @return
     */
    @GetMapping("/list/myTeam")
    public BaseResponse<List<TeamUserVO>> listMyTeam(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        teamQuery.setUserId(loginUser.getId());

        List<TeamUserVO> list = teamService.listTeam(teamQuery, true);
        return ResultUtils.success(list);
    }

    /**
     * 获取用户加入的队伍
     * @param teamQuery
     * @param request
     * @return
     */
    @GetMapping("/list/JoinTeam")
    public BaseResponse<List<TeamUserVO>> listJoinTeam(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
        // 取出不重复的队伍id
        List<Long> teamIdList = userTeamList.stream().map(UserTeam::getTeamId).distinct().collect(Collectors.toList());
        teamQuery.setIds(teamIdList);
        List<TeamUserVO> list = teamService.listTeam(teamQuery, true);
        return ResultUtils.success(list);
    }

}
