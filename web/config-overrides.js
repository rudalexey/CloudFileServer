const {override, fixBabelImports, addLessLoader, overrideDevServer, watchAll} = require('customize-cra');
const devServerConfig = () => config => {
    return {
        ...config,
        port: 3000,
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true,
                ws: false,
                secure: true,
            },

        },
    }
};
module.exports = {
    webpack: override(
        fixBabelImports('import', {
            libraryName: 'antd',
            libraryDirectory: 'es',
            style: true,
        }),
        addLessLoader({
            javascriptEnabled: true,
            modifyVars: {
                "@layout-body-background": 'url(data:image/png;base64,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) #FFFFFF',
                "@layout-header-background": "#f3f3f3",
                "@layout-header-height": "49px",
                "@layout-header-padding": "0 13px 0 0",
                "@layout-footer-background": "#2a2725",
                "@layout-footer-padding": "15px 13px 0",
                "@font-family": '"Open Sans", Arial, Helvetica, sans-serif',
                "@font-size-base": "13px",
                "@layout-sider-background": "#3a3633"
            },
        }),
    ),
    devServer: overrideDevServer(
        devServerConfig()
    )
};
