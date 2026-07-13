import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import { useAuthStore } from '../stores/authStore';
import DashboardView from '../views/DashboardView.vue';
import BuildingListView from '../views/BuildingListView.vue';
import RoomListView from '../views/RoomListView.vue';
import StudentListView from '../views/StudentListView.vue';
import StudentFormView from '../views/StudentFormView.vue';
import StaffListView from '../views/StaffListView.vue';
import SettingsView from '../views/SettingsView.vue';
import LoginView from '../views/LoginView.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    component: LoginView,
    meta: { requiresAuth: false },
  },
  {
    path: '/',
    redirect: '/dashboard',
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: DashboardView,
    meta: { requiresAuth: true },
  },
  {
    path: '/buildings',
    name: 'Buildings',
    component: BuildingListView,
    meta: { requiresAuth: true },
  },
  {
    path: '/rooms',
    name: 'Rooms',
    component: RoomListView,
    meta: { requiresAuth: true },
  },
  {
    path: '/students',
    name: 'Students',
    component: StudentListView,
    meta: { requiresAuth: true },
  },
  {
    path: '/students/new',
    name: 'AddStudent',
    component: StudentFormView,
    meta: { requiresAuth: true, roles: ['Manager'] }, // Only Admin/Manager
  },
  {
    path: '/students/edit/:id',
    name: 'EditStudent',
    component: StudentFormView,
    meta: { requiresAuth: true, roles: ['Manager'] }, // Only Admin/Manager
  },
  {
    path: '/staff',
    name: 'Staff',
    component: StaffListView,
    meta: { requiresAuth: true, roles: ['Manager'] }, // Only Admin/Manager
  },
  {
    path: '/settings',
    name: 'Settings',
    component: SettingsView,
    meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes: routes as unknown as RouteRecordRaw[],
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  if (to.meta.requiresAuth !== false && !authStore.isAuthenticated) {
    next('/login');
  } else if (to.path === '/login' && authStore.isAuthenticated) {
    next('/dashboard');
  } else if (
    to.meta.roles &&
    authStore.userRole &&
    !(to.meta.roles as string[]).includes(authStore.userRole)
  ) {
    alert('Unauthorized!');
    next(from.path && from.path !== '/login' ? from.path : '/dashboard'); // previous route or dashboard
  } else {
    next();
  }
});

export default router;
