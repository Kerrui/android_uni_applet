"use weex:vue";

if (typeof Promise !== 'undefined' && !Promise.prototype.finally) {
  Promise.prototype.finally = function(callback) {
    const promise = this.constructor
    return this.then(
      value => promise.resolve(callback()).then(() => value),
      reason => promise.resolve(callback()).then(() => {
        throw reason
      })
    )
  }
};

if (typeof uni !== 'undefined' && uni && uni.requireGlobal) {
  const global = uni.requireGlobal()
  ArrayBuffer = global.ArrayBuffer
  Int8Array = global.Int8Array
  Uint8Array = global.Uint8Array
  Uint8ClampedArray = global.Uint8ClampedArray
  Int16Array = global.Int16Array
  Uint16Array = global.Uint16Array
  Int32Array = global.Int32Array
  Uint32Array = global.Uint32Array
  Float32Array = global.Float32Array
  Float64Array = global.Float64Array
  BigInt64Array = global.BigInt64Array
  BigUint64Array = global.BigUint64Array
};


(()=>{var T=Object.create;var d=Object.defineProperty;var w=Object.getOwnPropertyDescriptor;var P=Object.getOwnPropertyNames;var C=Object.getPrototypeOf,_=Object.prototype.hasOwnProperty;var F=(t,e)=>()=>(e||t((e={exports:{}}).exports,e),e.exports);var v=(t,e,o,n)=>{if(e&&typeof e=="object"||typeof e=="function")for(let a of P(e))!_.call(t,a)&&a!==o&&d(t,a,{get:()=>e[a],enumerable:!(n=w(e,a))||n.enumerable});return t};var b=(t,e,o)=>(o=t!=null?T(C(t)):{},v(e||!t||!t.__esModule?d(o,"default",{value:t,enumerable:!0}):o,t));var g=F((J,D)=>{D.exports=Vue});var W=b(g());function c(t,e,...o){uni.__log__?uni.__log__(t,e,...o):console[t].apply(console,[...o,e])}var A=(t,e)=>{let o=t.__vccOpts||t;for(let[n,a]of e)o[n]=a;return o};var $=new Promise(()=>{});function y(t){typeof getApp().globalData.pluginTool.showFileDocumentView=="function"&&getApp().globalData.pluginTool.showFileDocumentView({},e=>{typeof t=="function"&&t(e.file_list)})}var q={zT:2,appId:plus.runtime.appid,appV:parseInt(plus.runtime.version.split(".").join("")),isAndroid:plus.os.name=="Android"};function r(t,e,o,n="Confirm"){m(t,e,o,"",n,!1)}function m(t,e,o,n,a="Confirm",l=!0,p="Cancel",s=!1,S){uni.showModal({title:t,content:e,showCancel:l,cancelText:p,confirmText:a,editable:s,placeholderText:S,success(f){f.confirm?typeof o=="function"&&o(f):f.cancel&&typeof n=="function"&&n()}})}var i=b(g());function V(t,e){typeof getApp().globalData.pluginTFLite.getNSFWScore=="function"&&getApp().globalData.pluginTFLite.getNSFWScore({filePath:t},o=>{typeof e=="function"&&e(o)})}function L(t,e){typeof getApp().globalData.pluginTFLite.faceDetector=="function"&&getApp().globalData.pluginTFLite.faceDetector({filePath:t},o=>{typeof e=="function"&&e(o)})}function O(t,e,o){typeof getApp().globalData.pluginTFLite.faceCompare=="function"&&getApp().globalData.pluginTFLite.faceCompare({filePath0:t,filePath1:e},n=>{typeof o=="function"&&o(n)})}var x={methods:{openFileSelector(t){y(e=>{let o=e[0],n="";if(t==0)n="\u9274\u9EC4";else if(t==1)n="\u4EBA\u8138";else if(t==2){if(n="\u5F62\u4F3C\u5EA6",e.length>=2){let a=e[0].path,l=e[1].path;O(a,l,p=>{r("\u662F\u5426\u540C\u4E00\u4EBA>0.8",JSON.stringify(p))})}else r("r\u5F62\u4F3C\u5EA6","\u8BF7\u9009\u62E9\u4E24\u5F20\u7167\u7247");return}m("\u56FE\u7247\u4FE1\u606F",o.path,()=>{t==0?V(o.path,a=>{c("log","at pagesPub/tflite-view.nvue:54",JSON.stringify(a)),r("\u9274\u9EC4",JSON.stringify(a))}):t==1&&L(o.path,a=>{r("\u4EBA\u8138",JSON.stringify(a))})},"",n)})}}};function I(t,e,o,n,a,l){let p=(0,i.resolveComponent)("button");return(0,i.openBlock)(),(0,i.createElementBlock)("scroll-view",{scrollY:!0,showScrollbar:!0,enableBackToTop:!0,bubble:"true",style:{flexDirection:"column"}},[(0,i.createElementVNode)("div",null,[(0,i.createVNode)(p,{onClick:e[0]||(e[0]=s=>l.openFileSelector(0))},{default:(0,i.withCtx)(()=>[(0,i.createTextVNode)("\u8272\u60C5\u68C0\u6D4B")]),_:1}),(0,i.createVNode)(p,{onClick:e[1]||(e[1]=s=>l.openFileSelector(1))},{default:(0,i.withCtx)(()=>[(0,i.createTextVNode)("\u4EBA\u8138\u68C0\u6D4B")]),_:1}),(0,i.createVNode)(p,{onClick:e[2]||(e[2]=s=>l.openFileSelector(2))},{default:(0,i.withCtx)(()=>[(0,i.createTextVNode)("\u4EBA\u8138\u5BF9\u6BD4")]),_:1})])])}var u=A(x,[["render",I]]);var h=plus.webview.currentWebview();if(h){let t=parseInt(h.id),e="pagesPub/tflite-view",o={};try{o=JSON.parse(h.__query__)}catch(a){}u.mpType="page";let n=Vue.createPageApp(u,{$store:getApp({allowDefault:!0}).$store,__pageId:t,__pagePath:e,__pageQuery:o});n.provide("__globalStyles",Vue.useCssStyles([...__uniConfig.styles,...u.styles||[]])),n.mount("#root")}})();
