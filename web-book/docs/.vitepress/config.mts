import { defineConfig } from 'vitepress'

// 用户中心 · 2026 软件工程实战书
// 文档站点配置:导航、侧边栏、本地搜索、中文本地化
export default defineConfig({
  lang: 'zh-CN',
  title: '用户中心 · 实战书',
  description: '通过「用户中心」一个项目,学会 2026 现代技术栈、软件工程方法论与团队协作。',

  // 部署到 GitHub Pages 的子路径(仓库名)
  base: '/user-center-team-project/',

  // 让构建在有占位 TODO 链接时不直接失败,便于团队渐进填充
  ignoreDeadLinks: true,

  lastUpdated: true,
  cleanUrls: true,

  head: [
    ['meta', { name: 'theme-color', content: '#6c63ff' }],
    ['meta', { name: 'author', content: '用户中心团队' }]
  ],

  themeConfig: {
    logo: undefined,

    nav: [
      { text: '开始', link: '/guide/preface' },
      { text: '理论篇', link: '/theory/' },
      { text: '实战篇', link: '/project/' },
      { text: '技术图鉴', link: '/stack/' },
      { text: '团队管理', link: '/engineering/' },
      { text: '文档工坊', link: '/docs-workshop/' }
    ],

    sidebar: {
      '/guide/': [
        {
          text: '① 开始',
          items: [
            { text: '序言:这本书是什么', link: '/guide/preface' },
            { text: '如何阅读', link: '/guide/how-to-read' },
            { text: '2026 技术全景图 ★', link: '/guide/tech-landscape' }
          ]
        }
      ],
      '/theory/': [
        {
          text: '② 软件工程理论篇',
          items: [
            { text: '本篇导读', link: '/theory/' },
            { text: '第1章 软件工程概述', link: '/theory/01-overview' },
            { text: '第2章 软件需求工程 ★', link: '/theory/02-requirements' },
            { text: '第3章 软件设计基础', link: '/theory/03-design' },
            { text: '第4章 结构化设计方法', link: '/theory/04-structured-design' },
            { text: '第5章 软件实现', link: '/theory/05-implementation' },
            { text: '第6章 软件测试', link: '/theory/06-testing' },
            { text: '第7章 UML 建模语言', link: '/theory/07-uml' },
            { text: '第8章 面向对象分析', link: '/theory/08-oo-analysis' },
            { text: '第9章 面向对象设计', link: '/theory/09-oo-design' },
            { text: '第10章 软件维护', link: '/theory/10-maintenance' },
            { text: '第11章 软件项目管理', link: '/theory/11-project-management' }
          ]
        }
      ],
      '/project/': [
        {
          text: '③ 用户中心实战篇(2026)',
          items: [
            { text: '本篇导读', link: '/project/' },
            { text: '需求分析', link: '/project/requirements' },
            { text: '数据库设计', link: '/project/database' },
            { text: '认证模块:注册与登录 ★', link: '/project/auth' },
            { text: '用户管理(管理员)', link: '/project/user-management' },
            { text: '后端架构与分层', link: '/project/backend' },
            { text: '前端架构', link: '/project/frontend' },
            { text: '测试', link: '/project/testing' },
            { text: '部署上线', link: '/project/deployment' }
          ]
        }
      ],
      '/stack/': [
        {
          text: '④ 技术栈图鉴',
          items: [
            { text: '本篇导读', link: '/stack/' },
            { text: 'Spring Boot ★', link: '/stack/spring-boot' },
            { text: 'Spring Security', link: '/stack/spring-security' },
            { text: 'MyBatis-Plus', link: '/stack/mybatis-plus' },
            { text: 'MySQL 8', link: '/stack/mysql' },
            { text: 'Redis', link: '/stack/redis' },
            { text: 'React 19 + Vite', link: '/stack/react-vite' },
            { text: 'Ant Design 5', link: '/stack/ant-design' },
            { text: 'TanStack Query', link: '/stack/tanstack-query' },
            { text: 'Docker Compose', link: '/stack/docker' }
          ]
        }
      ],
      '/engineering/': [
        {
          text: '⑤ 软件工程与团队管理',
          items: [
            { text: '本篇导读', link: '/engineering/' },
            { text: '软件生命周期', link: '/engineering/lifecycle' },
            { text: 'Git 协作与分支策略', link: '/engineering/git-workflow' },
            { text: '代码评审', link: '/engineering/code-review' },
            { text: 'AI 协作:Claude Code 工作流', link: '/engineering/ai-workflow' }
          ]
        }
      ],
      '/docs-workshop/': [
        {
          text: '⑥ 课程文档工坊',
          items: [
            { text: '本篇导读', link: '/docs-workshop/' }
          ]
        }
      ]
    },

    search: {
      provider: 'local',
      options: {
        translations: {
          button: { buttonText: '搜索', buttonAriaLabel: '搜索' },
          modal: {
            displayDetails: '显示详情',
            resetButtonTitle: '清除',
            backButtonTitle: '返回',
            noResultsText: '没有找到结果',
            footer: {
              selectText: '选择',
              navigateText: '切换',
              closeText: '关闭'
            }
          }
        }
      }
    },

    socialLinks: [
      { icon: 'github', link: 'https://github.com/wohuishuo/user-center-team-project' }
    ],

    docFooter: { prev: '上一页', next: '下一页' },
    outline: { label: '本页目录', level: [2, 3] },
    returnToTopLabel: '回到顶部',
    sidebarMenuLabel: '菜单',
    darkModeSwitchLabel: '主题',
    lightModeSwitchTitle: '切换到浅色模式',
    darkModeSwitchTitle: '切换到深色模式',

    lastUpdatedText: '最后更新',

    footer: {
      message: '一本通过项目学软件工程的书 · 2026 AI 时代版',
      copyright: '用户中心团队 · 《软件工程基础》课程项目'
    }
  }
})
