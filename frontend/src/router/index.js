import { createRouter, createWebHashHistory } from 'vue-router';

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      component: () => import('../components/pages/Index.vue'),
    },
    {
      path: '/authors',
      component: () => import('../components/ui/AuthorGrid.vue'),
    },
    {
      path: '/authors',
      component: () => import('../components/ui/AuthorGrid.vue'),
    },
    {
      path: '/eBooks',
      component: () => import('../components/ui/EBookGrid.vue'),
    },
    {
      path: '/lookupAuthors',
      component: () => import('../components/LookupAuthorView.vue'),
    },
    {
      path: '/lookupEbooks',
      component: () => import('../components/LookupEbookView.vue'),
    },
    {
      path: '/eBookPlatforms',
      component: () => import('../components/ui/EBookPlatformGrid.vue'),
    },
    {
      path: '/subscribers',
      component: () => import('../components/ui/SubscriberGrid.vue'),
    },
    {
      path: '/lookMyInfos',
      component: () => import('../components/LookMyInfoView.vue'),
    },
  ],
})

export default router;
