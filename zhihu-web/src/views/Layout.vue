<script setup>
import {
    Management,
    Promotion,
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom
} from '@element-plus/icons-vue';
import avatar from '@/assets/default.png'
import { StarFilled } from '@element-plus/icons-vue'

import { userInfoService } from '@/api/user';
import useUserInfoStore from '@/stores/userInfo.js'
import { useTokenStore } from '@/stores/token.js';
const tokenStore = useTokenStore();
const userInfoStore = useUserInfoStore();

// 调用函数，获取用户详细信息
const getUserInfo = async () => {
    //调用接口
    let result = await userInfoService();

    //数据存储到pinia中
    userInfoStore.setInfo(result.data)
}

getUserInfo();

import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
const router = useRouter()
// 条目被点击后被调用的函数
const handleCommand = (command) => {
    //判断指令
    if (command === 'logout') {
        //退出登陆
        ElMessageBox.confirm(
            '你确认要退出吗？',
            '温馨提示',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }
        )
            .then(async () => {
                // 退出登陆
                // 清空pinia中存储的token和个人信息
                tokenStore.removeToken();
                userInfoStore.removeInfo();

                //跳转到登陆页面
                router.push('/login')

                ElMessage({
                    type: 'success',
                    message: '退出登陆成功',
                })


            })
            .catch(() => {
                ElMessage({
                    type: 'info',
                    message: '用户取消了退出登陆',
                })
            })
    } else {
        //路由
        router.push('/user/' + command)
    }
}
</script>

<template>
    <!-- element-plus中的容器 -->
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px" style="border-radius: 26px; box-shadow: 0 0 15px; opacity: 50%;font-weight: bolder;">
            <div class="el-aside__logo"></div>
            <!-- element-plus的菜单标签 -->
            <el-menu activate-text-color="#ffd04b" background-color="#F2F6FC" text-color="#303133" router>
                <el-menu-item index="/article/category">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>文章分类</span>
                </el-menu-item>
                <el-menu-item index="/article/manage">
                    <el-icon>
                        <Promotion />
                    </el-icon>
                    <span>文章管理</span>
                </el-menu-item>
                <el-sub-menu>
                    <template #title>
                        <el-icon>
                            <UserFilled />
                        </el-icon>
                        <span>个人中心</span>
                    </template>
                    <el-menu-item index="/user/info">
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>基本资料</span>
                    </el-menu-item>
                    <el-menu-item index="/user/avatar">
                        <el-icon>
                            <Crop />
                        </el-icon>
                        <span>更换头像</span>
                    </el-menu-item>
                    <el-menu-item index="/user/resetPassword">
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>重置密码</span>
                    </el-menu-item>
                </el-sub-menu>
            </el-menu>
        </el-aside>
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header style="border-radius: 26px; box-shadow: 0 0 15px;margin-left: 10px;opacity: 50%;">
                <div>知友：<strong>{{ userInfoStore.info.nickname }}</strong></div>
                <!-- 下拉菜单 -->
                <!-- command:条目被点击后会触发，在事件函数上可以声明一个参数，接收条目对应的指令 -->
                <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="userInfoStore.info.userPic ? userInfoStore.info.userPic : avatar" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="info" :icon="User">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
                            <el-dropdown-item command="resetPassword" :icon="EditPen">重置密码</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">退出登陆</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-header>
            <!-- 中间区域 -->
            <el-main
                style="background-color: rgba(230,232,235,0.5);border-radius: 26px; box-shadow: 0 0 15px;margin-top: 10px;margin-left: 10px;">
                <!-- <div style="width: 1290px;height: 570px;border: 1px solid red;">
                    内容展示
                </div> -->
                <router-view></router-view>
            </el-main>
            <!-- 底部区域 -->
            <el-divider>
                <el-icon><star-filled /></el-icon>
            </el-divider>
            <el-footer>知乎 @2024 Created by lyw</el-footer>
        </el-container>
    </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
    height: 100vh;
    background: url("@/assets/background.png");
    .el-aside {
        background-color: rgba(242,246,252,0.5);

        &__logo {
            height: 120px;
            background: url('@/assets/logo.png') no-repeat center / 120px auto;
        }

        .el-menu {
            border-right: none;
        }
    }

    .el-header {
        background-color: rgba(255,255,255,0.5);
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: large;
        font-weight: bolder;
        color: white;
    }
}
</style>

