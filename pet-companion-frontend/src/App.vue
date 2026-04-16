<template>
  <div id="app">
    <!-- 路由视图，用于显示当前路由对应的组件 -->
    <router-view v-slot="{ Component }">
      <!-- 页面切换动画，使用fade效果 -->
      <transition name="fade" mode="out-in">
        <!-- 动态组件，根据当前路由显示对应的组件 -->
        <component :is="Component" />
      </transition>
    </router-view>
  </div>
</template>

<script setup>
// App组件，应用的根组件
// 使用script setup语法，简化组件定义
</script>

<style scoped>
/* 页面切换动画样式 */
/* 进入和离开动画的过渡效果 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

/* 进入开始和离开结束的状态 */
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
