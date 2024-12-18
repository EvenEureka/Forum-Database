<script setup>
import { ref } from 'vue'

const userPassword = ref({
    old_pwd: '',
    new_pwd: '',
    re_pwd: ''
})

const rules = {
    old_pwd: [
        { required: true, message: "请输入原密码", trigger: 'blur' },
    ],
    new_pwd: [
        { required: true, message: "请输入原密码", trigger: 'blur' },
    ],
    re_pwd: [
        { required: true, message: "请输入原密码", trigger: 'blur' },
    ],
}

//清空
const reset = () => {
    userPassword.value = {
        old_pwd: '',
        new_pwd: '',
        re_pwd: ''
    }
}

import { userUpdatePwdService } from '@/api/user.js'
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
const router = useRouter()
// 修改密码
const submit = async () => {
    let result = await userUpdatePwdService(userPassword.value)
    ElMessage.success(result.message ? result.message : "修改密码成功")

    reset();
    router.push('/login')
}
</script>

<template>
    <el-card shadow="always" style="background-color: rgba(255,255,255,0.5);font-weight: bolder;border-radius: 10px;">
        <template #header>
            <div class="header">
                <span>修改密码</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form label-position="top" label-width="100px" size="large" :model="userPassword" :rules="rules">
                    <el-form-item label="原密码" prop="old_pwd">
                        <el-input v-model="userPassword.old_pwd" type="password" show-password />
                    </el-form-item>
                    <el-form-item label="新密码" prop="new_pwd">
                        <el-input v-model="userPassword.new_pwd" type="password" show-password />
                    </el-form-item>
                    <el-form-item label="确认新密码" prop="re_pwd">
                        <el-input v-model="userPassword.re_pwd" type="password" show-password />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submit">
                            修改密码
                        </el-button>
                        <el-button @click="reset">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>


    </el-card>
</template>
  