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
  baseURL: 'http://localhost:8888',
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

    console.log('响应数据:', response.data);

    // 后端定义的成功状态码为200
    if (code === 200 && data !== null) {
      return data; // 直接返回data部分，简化使用
    } else if (code === 200 && data === null) {
      // 返回null的情况
      return null;
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
  // 直接指定返回类型为T，因为响应拦截器会处理为T类型
  const response = await service.get<T>(url, { params, ...config });
  // 直接返回response.data
  return response.data;
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
): Promise<T | null> => { // 明确返回类型可以是T或null
  try {
    const response = await service.post<T>(url, data, config);
    // 即使response.data是null也直接返回，不做默认值处理
    if (response === null) {
      return null;
    } else {
      return response.data;
    }
  } catch (error) {
    // 可以在这里统一处理错误，再抛给调用方
    console.error('请求失败:', error);
    throw error;
  }
};

export default service;
