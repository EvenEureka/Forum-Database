<script setup>
import {
    Delete,
    Edit,
} from '@element-plus/icons-vue'
import { ref } from 'vue'

// 文章分类数据模型
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

const tableRowClassName = ({ row }) => {
    if (row.state === "草稿") {
        return 'warning-row'
    } else if (row.state == "已发布") {
        return 'success-row'
    }
    return ''
}

//用户搜索时选中的分类id
const categoryId = ref('')

//用户搜索时选中的发布状态
const state = ref('')

//文章列表数据模型
const articles = ref([
    {
        "id": 2,
        "title": "天津旅游攻略",
        "content": "兵马俑",
        "coverImg": "https://zhihu.com",
        "state": "草稿",
        "categoryId": 1,
        "createTime": "2024-05-28 01:57:23",
        "updateTime": "2024-05-28 01:57:23"
    },
    {
        "id": 3,
        "title": "北京旅游攻略",
        "content": "故宫",
        "coverImg": "https://zhihu.com",
        "state": "已发布",
        "categoryId": 1,
        "createTime": "2024-05-28 01:47:23",
        "updateTime": "2024-05-28 01:47:23"
    },
    {
        "id": 4,
        "title": "广东旅游攻略",
        "content": "兵马俑",
        "coverImg": "https://zhihu.com",
        "state": "草稿",
        "categoryId": 1,
        "createTime": "2024-05-28 01:37:23",
        "updateTime": "2024-05-28 01:37:23"
    }
])

//分页条数据模型
const pageNum = ref(1)
const pageSize = ref(5)
// const small = ref(false)
// const background = ref(false)
// const disabled = ref(false)
const total = ref(20)

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    articleList();
}

//当每页条数发生了变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    articleList();
}

//回显文章分类
import { articleCategoryListService, articleListService, articleAddService,articleUpdateService, articleDeleteService } from '@/api/article.js'
const articleCategoryList = async () => {
    let result = await articleCategoryListService();

    categorys.value = result.data;
}

//获取文章列表数据
const articleList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value ? categoryId.value : null,
        state: state.value ? state.value : null
    }
    let result = await articleListService(params);
    //渲染视图
    total.value = result.data.total;
    articles.value = result.data.items;

    //处理数据，给数据模型扩展一个属性categoryName,分类名称
    for (let i = 0; i < articles.value.length; i++) {
        let article = articles.value[i]
        for (let j = 0; j < categorys.value.length; j++) {
            if (article.categoryId == categorys.value[j].id) {
                article.categoryName = categorys.value[j].categoryName
            }
        }
    }
}


articleCategoryList()
articleList();

// 添加文章抽屉组件
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { Plus } from '@element-plus/icons-vue'

//控制抽屉是否显示
const visibleDrawer = ref(false)

//添加表单数据模型
const articleModel = ref({
    title: '',
    categoryId: '',
    coverImg: '',
    content: '',
    state: ''
})

//添加文章表单校验
const rules = {
    title: [
        { required: true, message: "请输入文章标题", trigger: 'blur' }
    ],
    categoryId: [
        { required: true, message: "请选择文章分类", trigger: 'blur' }
    ],
    coverImg: [
        { required: true, message: "请上传文章封面", trigger: 'blur' }
    ],
    content: [
        { required: true, message: "请输入文章内容", trigger: 'blur' }
    ],
    
}

//导入token
import { useTokenStore } from '@/stores/token';
import { ElMessage,ElMessageBox } from 'element-plus';
const tokenStore = useTokenStore()
const drawerTitle = ref('')

//上传成功的回调函数
const uploadSuccess = (result) => {
    articleModel.value.coverImg = result.data;
    console.log(result.data);
}

const clearModel = () => {
    articleModel.value.title =''
    articleModel.value.categoryId =''
    articleModel.value.coverImg =''
    articleModel.value.content = ''
    articleModel.value.state =''
}

// 添加文章
const addArticle = async (clickState) => {
    //把发布状态赋值给数据模型
    articleModel.value.state = clickState;

    //调用接口
    let result = await articleAddService(articleModel.value)

    ElMessage.success(result.message ? result.message : '添加成功');

    //让抽屉消失
    visibleDrawer.value = false

    //刷新当前页表
    articleList()

}

// 编辑文章回显
const showArticle = (row)=>{
    visibleDrawer.value = true;

    drawerTitle.value = '编辑文章';
    //数据拷贝
    articleModel.value.title = row.title
    articleModel.value.content = row.content
    articleModel.value.categoryId = row.categoryId
    articleModel.value.coverImg = row.coverImg
    articleModel.value.state = row.state

    //扩展id数据，将来需要传递给后台，完成分类的修改
    articleModel.value.id = row.id
}

//编辑更新文章
const updateArticle = async (clickState) => {

    articleModel.value.state = clickState

    //调用接口
    let result = await articleUpdateService(articleModel.value)
    ElMessage.success(result.message ? result.message : "修改成功");

    //调用获取所有文章分类的函数并隐藏弹窗
    articleList();
    visibleDrawer.value = false;
}

// 删除文章
const deleteArticle = (row)=>{
    //提示用户 确认框
    ElMessageBox.confirm(
        '你确认要删除该文章吗？',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            // 调用接口
            let result = await articleDeleteService(row.id);
            ElMessage({
                type: 'success',
                message: '删除成功',
            })

            //刷新列表
            articleList();
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
    <el-card class="page-container" style="background-color: rgba(255,255,255,0.5);font-weight: bolder;border-radius: 10px;">
        <template #header>
            <div class="header">
                <span>文章管理</span>
                <el-button type="primary" @click="clearModel();visibleDrawer = true;drawerTitle='添加文章';">添加文章</el-button>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline class="demo-form-inline">
            <el-form-item label="文章分类">
                <el-select placeholder="请选择" v-model="categoryId">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="发布状态">
                <el-select placeholder="请选择" v-model="state">
                    <el-option label="已发布" value="已发布" />
                    <el-option label="草稿" value="草稿" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="articleList">搜索</el-button>
                <el-button type="default" @click="categoryId = ''; state = ''">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <div class="arti">
            <el-table :data="articles" style="width: 100%;border-radius: 10px;" :row-class-name="tableRowClassName">
                <el-table-column prop="title" label="文章标题" width="400" />
                <el-table-column prop="categoryName" label="分类" />
                <el-table-column prop="createTime" label="发表时间" />
                <el-table-column prop="state" label="状态" />
                <el-table-column label="操作" width="100">
                    <template #default="{ row }">
                        <el-button type="primary" :icon="Edit" circle plain @click="showArticle(row)"/>
                        <el-button type="danger" :icon="Delete" circle plain @click="deleteArticle(row)" />
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <!-- 分页条 -->
        <el-pagination class="el-p" v-model:current-page="pageNum" v-model:page-size="pageSize"
            :page-sizes="[5, 10, 15, 20]" layout="jumper, total, sizes, prev, pager, next" background :total="total"
            @size-change="onSizeChange" @current-change="onCurrentChange"
            style="margin-top: 20px; justify-content: flex-end;" />

        <!-- 抽屉 -->
        <el-drawer v-model="visibleDrawer" :title="drawerTitle" direction="rtl" size="50%">
            <!-- 添加文章表单 -->
            <el-form :model="articleModel" label-width="100px" :rules="rules">
                <el-form-item label="文章标题" prop="title">
                    <el-input v-model="articleModel.title" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="文章分类" prop="categoryId">
                    <el-select v-model="articleModel.categoryId" placeholder="请选择">
                        <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="文章封面" prop="coverImg">

                    <!-- 
                        auto-upload:设置是否自动上传
                        action:设置服务器接口路径
                        name:设置上传的文件字段名
                        headers:设置上传的请求头
                        on-sucess:设置上传成功的回调函数
                     -->

                    <el-upload class="avatar-uploader" :show-file-list="false" :auto-upload="ture" action="/api/upload"
                        name="file" :headers="{ 'Authorization': tokenStore.token }" :on-success="uploadSuccess">
                        <div class="elupload">
                            <img v-if="articleModel.coverImg" :src="articleModel.coverImg" class="avatar">
                            <el-icon v-else class="avatar-uploader-icon">
                                <Plus />
                            </el-icon>
                        </div>
                    </el-upload>
                </el-form-item>
                <el-form-item label="文章内容" prop="content">
                    <div class="editor">
                        <quill-editor theme="snow" v-model:content="articleModel.content" contentType="html">
                        </quill-editor>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="drawerTitle=='添加文章'? addArticle('已发布'):updateArticle('已发布')">发布</el-button>
                    <el-button type="info" @click="drawerTitle=='添加文章'?addArticle('草稿'):updateArticle('草稿')">草稿</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
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

    .arti {
        ::v-deep(.warning-row) {
            background-color: var(--el-color-warning-light-9) !important;
        }

        ::v-deep(.success-row) {
            background-color: var(--el-color-success-light-9) !important;
        }
    }

    .demo-form-inline .el-select {
        --el-select-width: 220px;
    }
}

/* 抽屉样式 */
.avatar-uploader {
    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }

    .elupload {
        border: 2px dashed var(--el-border-color);
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
        width: 178px;
        height: 178px;
        text-align: center;
    }
}

.ql-editor.ql-blank {
    min-height: 200px;
}

.editor {
    width: 100%;
}

:deep(.ql-editor) {
    min-height: 200px;
}

.el-table {
    // 表格边框的颜色，可以通过这个变量来设置表格的边框颜色。
    --el-table-border-color: var(--el-border-color-lighter);
    // 表格边框的样式，一般为实线或虚线，可以通过这个变量来设置表格的边框样式。
    --el-table-border: 1px solid var(--el-table-border-color);
    // 表格中文字的颜色，可以通过这个变量来设置表格中文字的颜色。
    --el-table-text-color: var(--el-text-color-regular);
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
  