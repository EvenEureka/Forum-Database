<script setup>
import { User, Lock } from "@element-plus/icons-vue";
import { ref } from "vue"

//控制注册与登陆表单的显示，默认显示登陆
const isRegister = ref(false)

//定义数据模型
const registerData = ref({
    username: '',
    password: '',
    rePassword: ''
})

//校验密码的函数
const checkRePassword = (rule, value, callback) => {
    if (value == '') {
        callback(new Error("请再次确认密码"))
    } else if (value != registerData.value.password) {
        callback(new Error("请确保两次输入的密码一样"))
    } else {
        callback()
    }

}

//定义表单校验规则
const rules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度为5~6位非空字符', trigger: 'blur' }
    ],
    rePassword: [
        { validator: checkRePassword, trigger: 'blur' }
    ]
}

//调用后台接口完成注册
import { userRegisterService, userLoginService } from '@/api/user.js'
import { ElMessage } from 'element-plus'

const register = async () => {
    //registerData是一个响应式对象，如果要获取值，需要.value
    let result = await userRegisterService(registerData.value);
    // if (result.code == 0) {
    //     //成功了
    //     alert(result.message ? result.message : "注册成功");
    // } else {
    //     alert('注册失败')
    // }
    // alert(result.message ? result.message : "注册成功");
    ElMessage.success(result.message ? result.message : "注册成功");
}

// 复用注册表单的数据模型
// 表单数据校验
// 登陆函数
import {useRouter} from 'vue-router'
import {useTokenStore} from '@/stores/token.js'

const router = useRouter()
const tokenStore = useTokenStore();
const login = async () => {
    // 调用接口完成登陆
    let result = await userLoginService(registerData.value);
    // if (result.code == 0) {
    //     //成功了
    //     alert(result.message ? result.message : "登陆成功");
    // } else {
    //     alert('登陆失败')
    // }
    // alert(result.message ? result.message : "登陆成功");
    ElMessage.success(result.message ? result.message : "登陆成功");
    //把得到的token存储到pinia中
    tokenStore.setToken(result.data);
    //跳转到首页 路由完成跳转
    router.push('/')
};

//定义函数用于清空数据模型的数据
const clearRegisterData = () => {
    registerData.value = {
        username: '',
        password: '',
        rePassword: ''
    }
}
</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg"></el-col>
        <el-col :span="6" :offset="3" class="form">
            <!-- 注册表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isRegister" :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>注册</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码"
                        v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item prop="rePassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请再次输入密码"
                        v-model="registerData.rePassword"></el-input>
                </el-form-item>
                <!-- 注册按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-sapce @click="register">
                        注册
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = false;clearRegisterData()">
                        返回
                    </el-link>
                </el-form-item>
            </el-form>
            <!-- 登陆表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-else :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>登陆</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码"
                        v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item class="flex">
                    <div class="flex">
                        <el-checkbox>记住我</el-checkbox>
                        <el-link type="primary" :underline="false">忘记密码？</el-link>
                    </div>
                </el-form-item>
                <!-- 登陆按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-sapce @click="login">
                        登陆
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = true;clearRegisterData()">
                        注册
                    </el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
    height: 100vh;
    background-color: #fff;

    .bg {
        background: url('@/assets/logo2.png') no-repeat 50% top / 240px auto,
            url('@/assets/login_bg.jpg') no-repeat center / cover;
        border-radius: 0 20px 20px 0;
    }

    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;

        .title {
            margin: 0 auto;
        }

        .button {
            width: 100%;
        }

        .flex {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }
}
</style>

