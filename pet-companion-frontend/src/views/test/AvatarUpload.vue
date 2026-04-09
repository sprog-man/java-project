<template>
  <div class="avatar-upload-container">
    <h1>头像上传测试</h1>
    <div class="upload-section">
      <el-upload
        class="avatar-uploader"
        action="http://localhost:8080/api/user/1/avatar"
        :headers="{ 'Authorization': token }"
        :show-file-list="false"
        :on-success="handleSuccess"
        :on-error="handleError"
        :before-upload="beforeUpload"
      >
        <img v-if="imageUrl" :src="imageUrl" class="avatar">
        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
      </el-upload>
      <div class="tips">
        点击上传头像，支持 JPG、PNG 格式，文件大小不超过 2MB
      </div>
    </div>
    <div v-if="imageUrl" class="result-section">
      <h3>上传成功！</h3>
      <p>头像 URL: {{ imageUrl }}</p>
      <img :src="imageUrl" class="preview-image">
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'

const imageUrl = ref('')
const token = ref('')

// 从localStorage获取token
onMounted(() => {
  token.value = localStorage.getItem('token') || ''
  // 如果没有token，使用测试token
  if (!token.value) {
    // 这里使用一个测试token，实际应用中应该通过登录获取
    token.value = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNzIwMjA4NjU4LCJpYXQiOjE3MjAyMDUwNTgsInVzZXJJZCI6MSwidXNlclR5cGUiOjAsInJvbGUiOjF9.9Q8eQ7y1e5Y4Y8Z9X6V3W2U1T0S7R5E4D3C2B1A'
  }
})

const handleSuccess = (response) => {
  if (response.code === 200) {
    // 构建完整的头像URL
    const baseUrl = 'http://localhost:8080/api'
    imageUrl.value = baseUrl + response.data.avatarUrl
    console.log('上传成功，头像URL:', imageUrl.value)
  } else {
    console.error('上传失败:', response.message)
  }
}

const handleError = (error) => {
  console.error('上传失败:', error)
}

const beforeUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('只能上传 JPG/PNG 格式的图片！')
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB！')
  }

  return isJPG && isLt2M
}
</script>

<style scoped>
.avatar-upload-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.upload-section {
  text-align: center;
  margin-bottom: 30px;
}

.avatar-uploader {
  width: 178px;
  height: 178px;
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  margin: 0 auto 20px;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.tips {
  color: #999;
  font-size: 14px;
}

.result-section {
  background-color: #e6f7ff;
  border: 1px solid #91d5ff;
  border-radius: 4px;
  padding: 20px;
  margin-top: 20px;
}

.result-section h3 {
  color: #1890ff;
  margin-bottom: 10px;
}

.preview-image {
  width: 150px;
  height: 150px;
  border-radius: 8px;
  margin-top: 10px;
}
</style>