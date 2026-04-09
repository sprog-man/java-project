<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <!-- 公司信息 -->
        <div class="footer-section">
          <h3>宠伴服务</h3>
          <p>专业的同城上门宠物陪伴服务平台，为您的爱宠提供贴心的照顾和陪伴。</p>
          <div class="social-links">
            <a href="#" class="social-link">
              <i class="icon wechat"></i>
            </a>
            <a href="#" class="social-link">
              <i class="icon weibo"></i>
            </a>
            <a href="#" class="social-link">
              <i class="icon phone"></i>
            </a>
          </div>
        </div>
        
        <!-- 快速链接 -->
        <div class="footer-section">
          <h4>快速链接</h4>
          <ul class="footer-links">
            <li><router-link to="/">首页</router-link></li>
            <li><router-link to="/service/type">服务类型</router-link></li>
            <li><router-link to="/order/create">预约服务</router-link></li>
            <li><router-link to="/user/center">个人中心</router-link></li>
          </ul>
        </div>
        
        <!-- 服务支持 -->
        <div class="footer-section">
          <h4>服务支持</h4>
          <ul class="footer-links">
            <li><a href="#">关于我们</a></li>
            <li><a href="#">服务条款</a></li>
            <li><a href="#">隐私政策</a></li>
            <li><a href="#">联系我们</a></li>
          </ul>
        </div>
        
        <!-- 联系信息 -->
        <div class="footer-section">
          <h4>联系我们</h4>
          <ul class="contact-info">
            <li>
              <i class="icon location"></i>
              <span>北京市朝阳区宠物大厦</span>
            </li>
            <li>
              <i class="icon phone"></i>
              <span>400-123-4567</span>
            </li>
            <li>
              <i class="icon email"></i>
              <span>service@petcompanion.com</span>
            </li>
          </ul>
        </div>
      </div>
      
      <!-- 版权信息 -->
      <div class="footer-bottom">
        <p>© 2026 宠伴服务平台. 保留所有权利.</p>
      </div>
    </div>
  </footer>
</template>

<script setup>
// Footer组件
</script>

<style scoped>
.footer {
  background-color: var(--primary-color);
  color: white;
  padding: var(--spacing-2xl) 0;
  margin-top: var(--spacing-2xl);
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-xl);
}

.footer-section h3 {
  font-size: 1.25rem;
  margin-bottom: var(--spacing-md);
  color: white;
}

.footer-section h4 {
  font-size: 1.125rem;
  margin-bottom: var(--spacing-md);
  color: white;
}

.footer-section p {
  margin-bottom: var(--spacing-md);
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.6;
}

.footer-links {
  list-style: none;
}

.footer-links li {
  margin-bottom: var(--spacing-sm);
}

.footer-links a {
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  transition: color var(--transition-fast);
}

.footer-links a:hover {
  color: white;
}

.social-links {
  display: flex;
  gap: var(--spacing-md);
  margin-top: var(--spacing-md);
}

.social-link {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  color: white;
  transition: all var(--transition-fast);
}

.social-link:hover {
  background-color: var(--cta-color);
  transform: translateY(-3px);
}

.contact-info {
  list-style: none;
}

.contact-info li {
  display: flex;
  align-items: flex-start;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-sm);
  color: rgba(255, 255, 255, 0.8);
}

.contact-info i {
  margin-top: 2px;
}

.footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding-top: var(--spacing-md);
  text-align: center;
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.875rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr;
    gap: var(--spacing-lg);
  }
  
  .footer-section {
    text-align: center;
  }
  
  .contact-info li {
    justify-content: center;
  }
  
  .social-links {
    justify-content: center;
  }
}
</style>
