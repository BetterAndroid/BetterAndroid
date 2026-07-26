type Locale = 'en' | 'zh-cn';

interface PageLinkRefs {
    dev: Record<string, string>[];
    prod: Record<string, string>[];
}

interface NavigationLink {
    path: string;
    title: Record<Locale, string>;
}

interface NavigationSection {
    title: Record<Locale, string>;
    links: NavigationLink[];
}

const navigationSections: NavigationSection[] = [{
    title: { en: 'Get Started', 'zh-cn': '入门' },
    links: [
        { path: '/guide/home', title: { en: 'Introduction', 'zh-cn': '介绍' } },
        { path: '/guide/quick-start', title: { en: 'Quick Start', 'zh-cn': '快速开始' } }
    ]
}, {
    title: { en: 'Libraries', 'zh-cn': '依赖' },
    links: [
        { path: '/library/android-bom', title: { en: 'android-bom', 'zh-cn': 'android-bom' } },
        { path: '/library/ui-component', title: { en: 'ui-component', 'zh-cn': 'ui-component' } },
        { path: '/library/ui-component-adapter', title: { en: 'ui-component-adapter', 'zh-cn': 'ui-component-adapter' } },
        { path: '/library/ui-extension', title: { en: 'ui-extension', 'zh-cn': 'ui-extension' } },
        { path: '/library/system-extension', title: { en: 'system-extension', 'zh-cn': 'system-extension' } },
        { path: '/library/permission-extension', title: { en: 'permission-extension', 'zh-cn': 'permission-extension' } },
        { path: '/library/compose-extension', title: { en: 'compose-extension', 'zh-cn': 'compose-extension' } },
        { path: '/library/compose-multiplatform', title: { en: 'compose-multiplatform', 'zh-cn': 'compose-multiplatform' } }
    ]
}, {
    title: { en: 'Configs', 'zh-cn': '配置' },
    links: [
        { path: '/config/basic', title: { en: 'Basic Configuration', 'zh-cn': '基本配置' } },
        { path: '/config/migration', title: { en: 'Migration Guide', 'zh-cn': '迁移指南' } },
        { path: '/config/r8-proguard', title: { en: 'R8 & Proguard Obfuscation', 'zh-cn': 'R8 与 Proguard 混淆' } },
        { path: '/config/lint-rules', title: { en: 'Lint Rules', 'zh-cn': 'Lint 静态检查规范' } }
    ]
}, {
    title: { en: 'About', 'zh-cn': '关于' },
    links: [
        { path: '/about/changelog', title: { en: 'Changelog', 'zh-cn': '更新日志' } },
        { path: '/about/future', title: { en: 'Looking Toward the Future', 'zh-cn': '展望未来' } },
        { path: '/about/contacts', title: { en: 'Contact Us', 'zh-cn': '联系我们' } },
        { path: '/about/about', title: { en: 'About This Document', 'zh-cn': '关于此文档' } }
    ]
}];

const topNavigationLinks: NavigationLink[] = [
    { path: '/', title: { en: 'Home', 'zh-cn': '首页' } },
    { path: '/guide/quick-start', title: { en: 'Quick Start', 'zh-cn': '快速开始' } },
    { path: '/about/changelog', title: { en: 'Changelog', 'zh-cn': '更新日志' } },
    { path: '/about/contacts', title: { en: 'Contact Us', 'zh-cn': '联系我们' } }
];

const localizedLink = (link: NavigationLink, locale: Locale) => ({
    text: link.title[locale],
    link: `/${locale}${link.path}`
});

/** Creates the VitePress navigation and sidebar for the requested locale. */
export const createThemeNavigation = (locale: Locale) => {
    const sections = navigationSections.map((section) => ({
        text: section.title[locale],
        items: section.links.map((link) => localizedLink(link, locale))
    }));
    return {
        nav: topNavigationLinks.map((link) => localizedLink(link, locale)),
        sidebar: {
            [`/${locale}/`]: sections.map((section) => ({
                text: section.text,
                collapsed: false,
                items: section.items
            }))
        }
    };
};

/** Defines shared site, development server, and repository settings. */
export const configs = {
    dev: {
        dest: '../dist',
        port: 9000
    },
    website: {
        base: '/BetterAndroid/',
        icon: '/BetterAndroid/images/logo.png',
        logo: '/images/logo.png',
        title: 'Better Android',
        locales: {
            en: {
                lang: 'en-US',
                description: 'Create more useful tool extensions for Android'
            },
            'zh-cn': {
                lang: 'zh-CN',
                description: '为 Android 创建更多有用的工具扩展'
            }
        }
    },
    github: {
        repo: 'https://github.com/BetterAndroid/BetterAndroid',
        page: 'https://betterandroid.github.io/BetterAndroid',
        branch: 'main',
        dir: 'docs-source/src'
    }
};

/** Defines custom Markdown link protocol replacements for each build mode. */
export const pageLinkRefs: PageLinkRefs = {
    dev: [
        { 'repo://': `${configs.github.repo}/` },
        // KDoc URL for local debugging, non-fixed value, adjust according to your own needs.
        // You can run ./build-dokka.sh and start the local server in dist/KDoc.
        { 'kdoc://': 'http://localhost:9001/' }
    ],
    prod: [
        { 'repo://': `${configs.github.repo}/` },
        { 'kdoc://': `${configs.github.page}/KDoc/` }
    ]
};