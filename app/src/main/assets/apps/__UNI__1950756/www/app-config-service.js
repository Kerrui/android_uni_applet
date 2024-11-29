
  ;(function(){
  let u=void 0,isReady=false,onReadyCallbacks=[],isServiceReady=false,onServiceReadyCallbacks=[];
  const __uniConfig = {"pages":[],"globalStyle":{"backgroundColor":"#F8F8F8","navigationBar":{"backgroundColor":"#F8F8F8","titleText":"uni-app","type":"default","titleColor":"#000000"},"isNVue":false},"nvue":{"compiler":"uni-app","styleCompiler":"uni-app","flex-direction":"column"},"renderer":"auto","appname":"plugin","splashscreen":{"alwaysShowBeforeRender":false,"autoclose":false},"compilerVersion":"3.99","entryPagePath":"pages/login/login","entryPageQuery":"","realEntryPagePath":"","networkTimeout":{"request":60000,"connectSocket":60000,"uploadFile":60000,"downloadFile":60000},"tabBar":{"position":"bottom","color":"#666666","selectedColor":"#000000","borderStyle":"#FFFFFF","blurEffect":"none","fontSize":"10px","iconWidth":"30px","spacing":"3px","height":"50px","backgroundColor":"#FFFFFF","list":[{"pagePath":"pages/user/index","iconPath":"/static/tab_user_n1.png","selectedIconPath":"/static/tab_user_s1.png"},{"pagePath":"pages/my/index","iconPath":"/static/tab_control_n1.png","selectedIconPath":"/static/tab_control_s1.png"}],"selectedIndex":0,"shown":true},"fallbackLocale":"en","locales":{},"darkmode":false,"themeConfig":{}};
  const __uniRoutes = [{"path":"pages/login/login","meta":{"isQuit":true,"isEntry":true,"navigationBar":{"titleText":"login","type":"default"},"isNVue":false}},{"path":"pages/user/index","meta":{"isQuit":true,"isTabBar":true,"tabBarIndex":0,"navigationBar":{"titleText":"Plugin","type":"default"},"isNVue":true}},{"path":"pages/my/index","meta":{"isQuit":true,"isTabBar":true,"tabBarIndex":1,"navigationBar":{"titleText":"My","type":"default"},"isNVue":false}},{"path":"pagesPub/screenshot-monitoring-result","meta":{"navigationBar":{"titleText":"监听 截屏/录屏 结果","type":"default"},"isNVue":false}},{"path":"pagesPub/album-selector","meta":{"navigationBar":{"titleText":"相册选择view","type":"default"},"isNVue":true}},{"path":"pagesPub/image-cache","meta":{"navigationBar":{"titleText":"图片缓存检测","type":"default"},"isNVue":true}},{"path":"pagesPub/mqtt-sub","meta":{"navigationBar":{"titleText":"MQTT 订阅检测","type":"default"},"isNVue":true}},{"path":"pagesPub/lottie-view","meta":{"navigationBar":{"titleText":"Lottie","type":"default"},"isNVue":true}},{"path":"pagesPub/tool-view","meta":{"navigationBar":{"titleText":"工具测试","type":"default"},"isNVue":true}},{"path":"pagesPub/log-view","meta":{"navigationBar":{"titleText":"LOG","type":"default"},"isNVue":true}},{"path":"pagesPub/user-log","meta":{"navigationBar":{"titleText":"用户日志信息","type":"default"},"isNVue":true}},{"path":"pagesPub/user-route-log","meta":{"navigationBar":{"titleText":"用户路由信息","type":"default"},"isNVue":true}},{"path":"pagesPub/audio-view","meta":{"navigationBar":{"titleText":"音频","type":"default"},"isNVue":true}},{"path":"pagesPub/event-view","meta":{"navigationBar":{"titleText":"event事件","type":"default"},"isNVue":true}},{"path":"pagesPub/adjust-view","meta":{"navigationBar":{"titleText":"adjust","type":"default"},"isNVue":true}},{"path":"pagesPub/open-install-view","meta":{"navigationBar":{"titleText":"OpenInstall","type":"default"},"isNVue":true}},{"path":"pagesPub/tflite-view","meta":{"navigationBar":{"titleText":"tflite","type":"default"},"isNVue":true}},{"path":"pagesPub/applet-view","meta":{"navigationBar":{"titleText":"小程序","type":"default"},"isNVue":true}}].map(uniRoute=>(uniRoute.meta.route=uniRoute.path,__uniConfig.pages.push(uniRoute.path),uniRoute.path='/'+uniRoute.path,uniRoute));
  __uniConfig.styles=[{}];//styles
  __uniConfig.onReady=function(callback){if(__uniConfig.ready){callback()}else{onReadyCallbacks.push(callback)}};Object.defineProperty(__uniConfig,"ready",{get:function(){return isReady},set:function(val){isReady=val;if(!isReady){return}const callbacks=onReadyCallbacks.slice(0);onReadyCallbacks.length=0;callbacks.forEach(function(callback){callback()})}});
  __uniConfig.onServiceReady=function(callback){if(__uniConfig.serviceReady){callback()}else{onServiceReadyCallbacks.push(callback)}};Object.defineProperty(__uniConfig,"serviceReady",{get:function(){return isServiceReady},set:function(val){isServiceReady=val;if(!isServiceReady){return}const callbacks=onServiceReadyCallbacks.slice(0);onServiceReadyCallbacks.length=0;callbacks.forEach(function(callback){callback()})}});
  service.register("uni-app-config",{create(a,b,c){if(!__uniConfig.viewport){var d=b.weex.config.env.scale,e=b.weex.config.env.deviceWidth,f=Math.ceil(e/d);Object.assign(__uniConfig,{viewport:f,defaultFontSize:16})}return{instance:{__uniConfig:__uniConfig,__uniRoutes:__uniRoutes,global:u,window:u,document:u,frames:u,self:u,location:u,navigator:u,localStorage:u,history:u,Caches:u,screen:u,alert:u,confirm:u,prompt:u,fetch:u,XMLHttpRequest:u,WebSocket:u,webkit:u,print:u}}}}); 
  })();
  