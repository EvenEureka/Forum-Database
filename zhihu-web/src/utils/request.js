//定制请求的示例
import axios from 'axios'

// const baseURL = "http://localhost:8080"
const baseURL = "/api"
const instance = axios.create({baseURL})
import { ElMessage } from 'element-plus'
import { useTokenStore } from '@/stores/token'

//添加请求拦截器
instance.interceptors.request.use(
    config=>{
        //请求前回调
        //添加token
        const tokenStore = useTokenStore();
        //判断有没有token
        if(tokenStore.token){
            config.headers.Authorization=tokenStore.token;
        }
        return config;
    },
    err=>{
        //请求错误的回调
        Promise.reject(err);
    }
)

// 导入失败
// import { useRouter } from 'vue-router'
// const router = useRouter();
import router from '@/router'

//添加响应拦截器
instance.interceptors.response.use(
    result =>{
        //判断业务状态码
        if(result.data.code==0){
            return result.data;
        }

        //操作失败
        // alert(result.data.message?result.data.message:"服务异常");
        ElMessage.error(result.data.message?result.data.message:"服务异常");
        //异步操作的状态转换为失败
        return Promise.reject(err);
    },
    err=>{
        //判断响应状态码，如果为401，则证明未登录，提示请登陆并跳转到登陆页面
        if(err.response.status===401){
            ElMessage.error("请先登陆");
            router.push('/login');
        }
        else{
            ElMessage.error("服务异常")
        }
        return Promise.reject(err);
    }
)

export default instance