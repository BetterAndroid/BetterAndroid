import type giscusTalk from 'vitepress-plugin-comment-with-giscus';

type GiscusOptions = Parameters<typeof giscusTalk>[0];

/** Lists source pages that must not mount the Giscus comment section. */
export const giscusExcludedPages = [
    'en/about/about.md',
    'zh-cn/about/about.md'
];

/** Defines the GitHub Discussions repository, category, and localized Giscus behavior. */
export const giscusOptions = {
    repo: 'BetterAndroid/BetterAndroid',
    repoId: 'R_kgDOKjOIBg',
    category: 'General',
    categoryId: 'DIC_kwDOKjOIBs4DB-wa',
    inputPosition: 'bottom',
    locales: {
        'en-US': 'en',
        'zh-CN': 'zh-CN'
    },
    homePageShowComment: false
} satisfies GiscusOptions;