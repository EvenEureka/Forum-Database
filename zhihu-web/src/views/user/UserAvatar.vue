<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import { ref } from 'vue';
import avatar from '@/assets/default.png'
import useUserInfoStore from '@/stores/userInfo';
import { useTokenStore } from '@/stores/token';
import {userAvatarUpdateService } from '@/api/user.js'
import { ElMessage } from 'element-plus';

const tokenStore = useTokenStore()
const userInfoStore = useUserInfoStore()
const uploadRef = ref()

// 用户头像地址
const imgUrl = ref(userInfoStore.info.userPic)

//图片上传成功的回调函数
const uploadSuccess = (result)=>{
    imgUrl.value = result.data;
}

//修改头像
const updateAvatar = async()=>{
    let result = await userAvatarUpdateService(imgUrl.value);

    ElMessage.success(result.message?result.message:"修改成功");

    //修改pinia中的数据
    userInfoStore.info.userPic = imgUrl.value;
}
</script>

<template>
    <el-card calss="page-container" style="background-color: rgba(255,255,255,0.5);font-weight: bolder;border-radius: 10px;">
        <template #header>
            <div class="header">
                <span>更换头像</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-upload ref="uploadRef" class="avatar-uploader" :show-file-list="false"
                    :auto-upload="true" action="/api/upload" name="file" :headers="{'Authorization':tokenStore.token}"
                    :on-success="uploadSuccess"
                >
                    <div class="elupload">
                        <img v-if="imgUrl" :src="imgUrl" class="avatar" />
                        <img v-else :src="avatar" width="278" />
                    </div>
                </el-upload>
                <br />
                <el-button type="primary" size="large" :icon="Plus" @click="uploadRef.$el.querySelector('input').click()">
                    选择图片
                </el-button>
                <el-button type="success" size="large" :icon="Upload" @click="updateAvatar">
                    上传头像
                </el-button>

            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped>
.avatar-uploader {
    .avatar {
        width: 278px;
        height: 278px;
        display: block;
    }

    .elupload {
        border: 1px dashed var(--el-border-color);
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        transition: var(--el-transition-duration-fast);
    }

    .elupload:hover {
        border-color: var(--el-color-primary);
    }

    .el-icon.avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 278px;
        height: 278px;
        text-align: center;
    }
}
</style>