<template>
  <div class="page-container">
    <div class="card-container">
      <el-card style="max-width: 480px">
        <el-form
        ref="ruleFormRef"
        :model="User"
        :rules="currentRules"
        label-width="auto"
        scroll-to-error="true"
        >
            <el-form-item>
                <div class="form-title">{{ isLoginMode ? '登录' : '注册' }}</div>
            </el-form-item>
            <el-form-item label="账号：" prop="username">
                <el-input v-model="User.username" placeholder="请输入你的账号"/>
            </el-form-item>
            <el-form-item label="密码：" prop="password">
                <el-input v-model="User.password" placeholder="请输入你的密码"/>
            </el-form-item>
            <el-form-item v-if="!isLoginMode" label="邮箱：" prop="email">
                <el-input v-model="User.email" placeholder="请输入你的邮箱"/>
            </el-form-item>
            <el-form-item v-if="!isLoginMode" label="手机号：" prop="phone">
                <el-input v-model="User.phone" placeholder="请输入你的手机号"/>
            </el-form-item>

            <div class="bottom-links">
              <el-form-item>
                <el-button @click="toggleMode" :type="!isLoginMode ? 'primary' : 'success'" link>
                        {{ isLoginMode ? '我要注册' : '现在登录' }}
                </el-button>
                <el-button @click="forgotPassword" type="danger" link v-if="isLoginMode">
                    忘记密码
                </el-button>
              </el-form-item>
          </div>

            <div class="button-wrapper">
                <el-form-item>
                    <el-button :type="isLoginMode ? 'primary' : 'success'" @click="submitForm(ruleFormRef)">{{ isLoginMode ? '登 录' : '立即注册' }}</el-button>
                </el-form-item>
            </div>
        </el-form>
      </el-card>
    </div>

    <!-- 背景形状容器 -->
    <div class="background-shapes">
      <!-- 圆形 -->
      <div class="shape circle" style="top: 15%; left: 10%; width: 80px; height: 80px;"></div>
      <div class="shape circle" style="top: 60%; left: 85%; width: 120px; height: 120px;"></div>
      <div class="shape circle" style="top: 80%; left: 25%; width: 50px; height: 50px;"></div>

      <!-- 矩形 -->
      <div class="shape rectangle" style="top: 25%; left: 80%; width: 100px; height: 60px; transform: rotate(15deg);"></div>
      <div class="shape rectangle" style="top: 70%; left: 15%; width: 70px; height: 70px; transform: rotate(-30deg);"></div>
      <div class="shape rectangle" style="top: 40%; left: 5%; width: 60px; height: 100px; transform: rotate(45deg);"></div>

      <!-- 三角形 -->
      <div class="shape triangle" style="top: 10%; left: 60%; width: 0; height: 0; border-left: 50px solid transparent; border-right: 50px solid transparent; border-bottom: 86px solid white; transform: rotate(20deg);"></div>
      <div class="shape triangle" style="top: 50%; left: 90%; width: 0; height: 0; border-left: 30px solid transparent; border-right: 30px solid transparent; border-bottom: 52px solid white; transform: rotate(-15deg);"></div>
      <div class="shape triangle" style="top: 90%; left: 70%; width: 0; height: 0; border-left: 40px solid transparent; border-right: 40px solid transparent; border-bottom: 69px solid white; transform: rotate(160deg);"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed } from 'vue'

import { post } from '../request/request';

import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus';

interface User {
    username: string
    password: string
    email: string
    phone: string
}

const ruleFormRef = ref<FormInstance>();
const isLoginMode = ref(true); // 默认为登录模式
const User = reactive<User>({
  username: '',
  password: '',
  email: '',
  phone: ''
});

// 基础规则
const baseRules = reactive<FormRules<User>>({
    username: [
        { required: true, message: '用户名不能为空', trigger: 'blur' },
        { min: 3, max: 15, message: '长度应在3到15之间', trigger: 'blur' },
    ],
    password: [
        { required: true, message: '密码不能为空', trigger: 'blur' },
        { min: 6, max: 20, message: '长度应在6到20之间', trigger: 'blur' },
    ],
});

// 注册额外规则
const registerRules = reactive<FormRules<User>>({
    email: [
        { 
            pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, 
            message: '请输入有效的邮箱地址', 
            trigger: 'blur' 
        },
    ],
    phone: [
        { 
            pattern: /^1[3-9]\d{9}$/, 
            message: '请输入有效的手机号', 
            trigger: 'blur' 
        },
    ],
});

// 根据模式动态计算当前规则
const currentRules = computed(() => {
    if (isLoginMode.value) {
        return baseRules;
    } else {
        return { ...baseRules, ...registerRules };
    }
});

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (isLoginMode.value) {
        console.log('登录提交!', User.username, User.password);
        // 这里添加登录逻辑
      } else {
        // 使用封装的post方法发送注册请求
        post('/user/add', User)
          .then(data => {
            // 明确处理data为null的情况
            ElMessage({
              message: '注册成功注册成功，即将跳转到登录',
              type: 'success'
            });
            isLoginMode.value = true;
            resetForm(ruleFormRef.value);
          })
          .catch(error => {
            // 错误处理保持不变
            console.error('注册注册失败，错误详情:', error);
            ElMessage({
              message: error.message || '注册失败，请重试',
              type: 'error'
            });
          });
      }
    } else {
      console.log('error submit!', fields);
    }
  });
};

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.resetFields();
};

// 切换登录/注册模式
const toggleMode = () => {
    isLoginMode.value = !isLoginMode.value;
    resetForm(ruleFormRef.value); // 切换模式时重置表单
};

// 添加忘记密码处理函数
const forgotPassword = () => {
  console.log('忘记密码');
  // 这里可以添加跳转到忘记密码页面的逻辑
};
</script>

<style scoped>
/* 整个窗口的背景图容器 */
.page-container {
  /* 占满整个窗口 */
  width: 100vw;
  height: 100vh;
  /* 设置对角方向背景渐变色 (从左上角到右下角) */
  background: linear-gradient(135deg, #b2ebf2, #e0f7fa);
  /* 设置背景图片 */
  /* background-image: url('../assets/bg.jpg'); */
  /* 背景图铺满容器 */
  background-size: cover;
  /* 背景图居中 */
  background-position: center;
  /* 背景图不重复 */
  background-repeat: no-repeat;
  /* 可选：添加半透明遮罩，提升文字可读性 */
  position: relative;
  overflow: hidden;
}

/* 半透明遮罩（可选，根据图片明暗调整） */
.page-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

/* 卡片居中容器 */
.card-container {
  /* 确保卡片在遮罩之上 */
  position: relative;
  z-index: 2;
  /* 居中逻辑不变 */
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

/* 标题样式 */
.form-title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 25px;
  font-weight: bold;
  color: #333;
  width: 100%;
}

/* 按钮居中样式不变 */
.button-wrapper {
  display: flex;
  justify-content: center;
}

:deep(.el-card) {
  width: 100%;
  border-radius: 8px;
  border: none;
  position: relative;
}

:deep(.el-form-item) {
  display: flex;
  justify-content: center;
}

/* 调整按钮样式 */
.button-wrapper :deep(.el-button) {
  margin: 0 10px;
}

/* 右下角链接样式 */
.bottom-links {
  position: relative; /* 修改为相对定位 */
  margin-top: 15px; /* 增加上边距 */
  display: flex;
  justify-content: flex-end; /* 右对齐 */
  gap: 15px;
}

/* 背景形状容器 - 确保在最底层 */
.background-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
}

/* 基础形状样式 */
.shape {
  position: absolute;
  background-color: white;
  opacity: 0.3; /* 增加不透明度使其更明显 */
}

/* 圆形样式 */
.circle {
  width: 80px;
  height: 80px;
  border-radius: 50%;
}

/* 矩形样式 */
.rectangle {
  width: 100px;
  height: 60px;
}

/* 三角形样式 */
.triangle {
  width: 0;
  height: 0;
  border-left: 50px solid transparent;
  border-right: 50px solid transparent;
  border-bottom: 86px solid white;
  background-color: transparent;
}
</style>