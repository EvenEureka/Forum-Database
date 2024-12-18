<script setup>
import {
    Edit,
    Delete
} from "@element-plus/icons-vue"
import { ref } from 'vue'

const categorys = ref([
    {
        "id": 3,
        "categoryName": "美食",
        "categoryAlias": 'my',
        "createTime": '2023-09-02 12:06:59',
        "updateTime": '2023-09-02 12:06:59',
    },
    {
        "id": 4,
        "categoryName": "娱乐",
        "categoryAlias": 'yl',
        "createTime": '2023-09-02 11:06:59',
        "updateTime": '2023-09-02 11:06:59',
    },
    {
        "id": 5,
        "categoryName": "军事",
        "categoryAlias": 'js',
        "createTime": '2023-09-02 10:06:59',
        "updateTime": '2023-09-02 10:06:59',
    }
])
// 声明一个异步函数
import { articleCategoryListService, articleCategoryAddService, articleCategoryUpdateService, articleCategoryDeleteService } from '@/api/article.js'
const articleCategoryList = async () => {
    let result = await articleCategoryListService();
    categorys.value = result.data;
}
articleCategoryList();
//控制添加分类弹窗
const dialogVisible = ref(false)

// 添加分类数据模型
const categoryModel = ref({
    categoryName: '',
    categoryAlias: ''
})

//添加分类表单校验
const rules = {
    categoryName: [
        { required: true, message: "请输入分类名称", trigger: 'blur' }
    ],
    categoryAlias: [
        { required: true, message: "请输入分类别名", trigger: 'blur' }
    ]
}

import { ElMessage, ElMessageBox } from "element-plus";
// 调用接口，添加表单
const addCategory = async () => {
    // 调用接口
    let result = await articleCategoryAddService(categoryModel.value);
    ElMessage.success(result.message ? result.message : "添加成功");

    //调用获取所有文章分类的函数
    articleCategoryList();
    dialogVisible.value = false;
}

//定义变量，控制标题的展示
const title = ref('')

//展示编辑弹窗
const showDialog = (row) => {
    dialogVisible.value = true;
    title.value = '编辑分类';
    //数据拷贝
    categoryModel.value.categoryName = row.categoryName
    categoryModel.value.categoryAlias = row.categoryAlias
    //扩展id数据，将来需要传递给后台，完成分类的修改
    categoryModel.value.id = row.id
}

//编辑分类
const updateCategory = async () => {
    //调用接口
    let result = await articleCategoryUpdateService(categoryModel.value)
    ElMessage.success(result.message ? result.message : "修改成功");

    //调用获取所有文章分类的函数并隐藏弹窗
    articleCategoryList();
    dialogVisible.value = false;
}

// 清空模型数据
const clearData = () => {
    categoryModel.value.categoryName = '';
    categoryModel.value.categoryAlias = '';
}

//删除分类
const deleteCategory = (row) => {
    //提示用户 确认框
    ElMessageBox.confirm(
        '你确认要删除该分类信息吗？',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            // 调用接口
            let result = await articleCategoryDeleteService(row.id);
            ElMessage({
                type: 'success',
                message: '删除成功',
            })

            //刷新列表
            articleCategoryList();
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '用户取消了删除',
            })
        })
}
</script>

<template>
    <el-card shadow="always" class="page-container" style="background-color: rgba(255,255,255,0.5);font-weight: bolder;border-radius: 10px;">
        <template #header>
            <div class="header">
                <span>文章分类</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible = true; title = '添加分类'; clearData()">添加分类</el-button>
                </div>
            </div>
        </template>
        <el-table :data="categorys" style="width: 100%;border-radius: 10px;">
            <el-table-column label="序号" width="100" type="index"></el-table-column>
            <el-table-column label="分类名称" prop="categoryName"></el-table-column>
            <el-table-column label="分类别名" prop="categoryAlias"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteCategory(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!-- 添加分类弹窗 -->
        <el-dialog v-model="dialogVisible" :title="title" width="30%">
            <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px;">
                <el-form-item label="分类名称" prop="categoryName">
                    <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="分类别名" prop="categoryAlias">
                    <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="15"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="title == '添加分类' ? addCategory() : updateCategory()"> 确认 </el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}

.el-table {
    // 表格边框的颜色，可以通过这个变量来设置表格的边框颜色。
    --el-table-border-color: var(--el-border-color-lighter);
    // 表格边框的样式，一般为实线或虚线，可以通过这个变量来设置表格的边框样式。
    --el-table-border: 1px solid var(--el-table-border-color);
    // 表格中文字的颜色，可以通过这个变量来设置表格中文字的颜色。
    --el-table-text-color: black;
    // 表头中文字的颜色，可以通过这个变量来设置表头中文字的颜色。
    --el-table-header-text-color: black;
    // 鼠标悬停在行上时的背景色，可以通过这个变量来设置鼠标悬停时行的背景色。
    --el-table-row-hover-bg-color: rgba(14, 95, 255, 0.5);
    // 当前行的背景色，可以通过这个变量来设置当前行的背景色。
    --el-table-current-row-bg-color: var(--el-fill-color-light);
    // 表头的背景色，可以通过这个变量来设置表头的背景色。
    --el-table-header-bg-color: rgba(6,187,154,0.5);
    // 固定列的阴影样式，可以通过这个变量来设置固定列的阴影样式。
    --el-table-fixed-box-shadow: var(--el-box-shadow-light);
    // 表格的背景色，可以通过这个变量来设置表格的背景色。
    --el-table-bg-color: rgba(225,234,243,0.5);
    // 表格行的背景色，可以通过这个变量来设置表格行的背景色。
    --el-table-tr-bg-color:rgba(225,234,243,0.5);
    // 展开行的背景色，可以通过这个变量来设置展开行的背景色。
    --el-table-expanded-cell-bg-color: var(--el-fill-color-blank);
    // 左侧固定列的阴影样式，可以通过这个变量来设置左侧固定列的阴影样式。
    --el-table-fixed-left-column: inset 10px 0 10px -10px rgba(0, 0, 0, 0.15);
    // 右侧固定列的阴影样式，可以通过这个变量来设置右侧固定列的阴影样式。
    --el-table-fixed-right-column: inset -10px 0 10px -10px rgba(0, 0, 0, 0.15);
}
</style>