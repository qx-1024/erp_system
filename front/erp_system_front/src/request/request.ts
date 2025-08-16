import axios from 'axios';
import type { 
  AxiosInstance, 
  InternalAxiosRequestConfig,
  AxiosResponse, 
  AxiosError 
} from 'axios';
import { ElMessage } from 'element-plus';

// 定义后端返回的统一格式类型
interface ApiResponse<T = any> {
    code: number;
    message: string;
    data: T;
    extra: Record<string, any>;
    timestamp: number;
  }

// 创建axios实例
const service: AxiosInstance = axios.create({
    // baseURL: import.meta.env.VITE_API_BASE_URL,  // 从环境变量获取
    baseURL: 'http://localhost:8888/api',
    timeout: 10000,
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    }
  });

// 请求拦截器
service.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
      // 添加Token
      const token = localStorage.getItem('token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    },
    (error: AxiosError) => {
      console.error('请求配置错误:', error);
      ElMessage.error('请求发送失败，请稍后重试');
      return Promise.reject(error);
    }
  );
  
  // 响应拦截器
  service.interceptors.response.use(
    (response: AxiosResponse<ApiResponse>) => {
      const { code, message, data } = response.data;
      
      // 后端定义的成功状态码为0
      if (code === 0) {
        return data; // 直接返回data部分，简化使用
      } else {
        // 业务错误处理
        ElMessage.error(message || '操作失败');
        return Promise.reject(new Error(message || `错误码: ${code}`));
      }
    },
    (error: AxiosError) => {
      console.error('响应错误:', error);
      
      // 网络或服务器错误处理
      if (error.response) {
        switch (error.response.status) {
          case 401:
            ElMessage.error('登录已过期，请重新登录');
            // 可添加跳转到登录页的逻辑
            break;
          case 403:
            ElMessage.error('没有权限执行该操作');
            break;
          case 404:
            ElMessage.error('请求的资源不存在');
            break;
          case 500:
            ElMessage.error('服务器内部错误');
            break;
          default:
            ElMessage.error(`请求错误: ${error.response.status}`);
        }
      } else if (error.request) {
        ElMessage.error('服务器无响应，请检查网络');
      } else {
        ElMessage.error('请求失败，请稍后重试');
      }
      
      return Promise.reject(error);
    }
  );
  
/**
 * GET请求封装
 * @param url 请求地址
 * @param params 查询参数
 * @param config 额外配置
 * @returns 后端返回的data部分（T类型）
 */
export const get = async <T = any>(
    url: string,
    params?: Record<string, any>,
    config?: InternalAxiosRequestConfig
  ): Promise<T> => {
    // 先获取完整响应体（AxiosResponse<ApiResponse<T>>）
    const response = await service.get<ApiResponse<T>>(url, { params, ...config });
    // 提取后端返回的ApiResponse对象，再返回其data属性（业务数据）
    return response.data.data;
  };
  
  /**
   * POST请求封装
   * @param url 请求地址
   * @param data 请求体数据
   * @param config 额外配置
   * @returns 后端返回的data部分（T类型）
   */
  export const post = async <T = any>(
    url: string,
    data?: Record<string, any>,
    config?: InternalAxiosRequestConfig
  ): Promise<T> => {
    // 先获取完整响应体
    const response = await service.post<ApiResponse<T>>(url, data, config);
    // 提取业务数据
    return response.data.data;
  };

export default service;
