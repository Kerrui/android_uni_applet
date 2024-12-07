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


(()=>{var T=Object.create;var d=Object.defineProperty;var _=Object.getOwnPropertyDescriptor;var w=Object.getOwnPropertyNames;var P=Object.getPrototypeOf,v=Object.prototype.hasOwnProperty;var C=(t,e)=>()=>(e||t((e={exports:{}}).exports,e),e.exports);var F=(t,e,o,i)=>{if(e&&typeof e=="object"||typeof e=="function")for(let a of w(e))!v.call(t,a)&&a!==o&&d(t,a,{get:()=>e[a],enumerable:!(i=_(e,a))||i.enumerable});return t};var y=(t,e,o)=>(o=t!=null?T(P(t)):{},F(e||!t||!t.__esModule?d(o,"default",{value:t,enumerable:!0}):o,t));var c=C((J,b)=>{b.exports=Vue});var W=y(c());function f(t,e,...o){uni.__log__?uni.__log__(t,e,...o):console[t].apply(console,[...o,e])}var D=(t,e)=>{let o=t.__vccOpts||t;for(let[i,a]of e)o[i]=a;return o};var j=new Promise(()=>{});function A(t){typeof getApp().globalData.pluginTool.showFileDocumentView=="function"&&getApp().globalData.pluginTool.showFileDocumentView({},e=>{typeof t=="function"&&t(e.file_list)})}var E={zT:2,appId:plus.runtime.appid,appV:parseInt(plus.runtime.version.split(".").join("")),isAndroid:plus.os.name=="Android"};function r(t,e,o,i="Confirm"){m(t,e,o,"",i,!1)}function m(t,e,o,i,a="Confirm",l=!0,p="Cancel",s=!1,S){uni.showModal({title:t,content:e,showCancel:l,cancelText:p,confirmText:a,editable:s,placeholderText:S,success(g){g.confirm?typeof o=="function"&&o(g):g.cancel&&typeof i=="function"&&i()}})}var n=y(c());function V(t,e){typeof getApp().globalData.pluginTFLite.getNSFWScore=="function"&&getApp().globalData.pluginTFLite.getNSFWScore({filePath:t},o=>{typeof e=="function"&&e(o)})}function x(t,e){typeof getApp().globalData.pluginTFLite.faceDetector=="function"&&getApp().globalData.pluginTFLite.faceDetector({filePath:t},o=>{typeof e=="function"&&e(o)})}function L(t,e,o){typeof getApp().globalData.pluginTFLite.faceCompare=="function"&&getApp().globalData.pluginTFLite.faceCompare({filePath0:t,filePath1:e},i=>{typeof o=="function"&&o(i)})}function O(t,e){typeof getApp().globalData.pluginTFLite.showFaceVideoView=="function"&&getApp().globalData.pluginTFLite.showFaceVideoView(t,o=>{typeof e=="function"&&e(o)})}var k={methods:{openFileSelector(t){A(e=>{let o=e[0],i="";if(t==0)i="\u9274\u9EC4";else if(t==1)i="\u4EBA\u8138";else if(t==2){if(i="\u5F62\u4F3C\u5EA6",e.length>=2){let a=e[0].path,l=e[1].path;L(a,l,p=>{r("\u662F\u5426\u540C\u4E00\u4EBA>0.8",JSON.stringify(p))})}else r("\u5F62\u4F3C\u5EA6","\u8BF7\u9009\u62E9\u4E24\u5F20\u7167\u7247");return}m("\u56FE\u7247\u4FE1\u606F",o.path,()=>{t==0?V(o.path,a=>{f("log","at pagesPub/tflite-view.nvue:56",JSON.stringify(a)),r("\u9274\u9EC4",JSON.stringify(a))}):t==1&&x(o.path,a=>{r("\u4EBA\u8138",JSON.stringify(a))})},"",i)})},liveness(){O({is_hide_ready_page:!1,start_page_config:{title:"Center your face",check_text:"Start video check"},check_page_config:{blink_text:"Please blink",shake_text:"Please shake your head slowly",mouth_text:"Please open your mouth",check_multiple_text:"Multiple faces detected",check_not_face_text:"Face not detected, please move face to center of screen",check_fail:"Liveness detection failed, please try again",check_success:"Liveness detection passed"}},e=>{f("log","at pagesPub/tflite-view.nvue:87",e),r("liveness",JSON.stringify(e))})}}};function N(t,e,o,i,a,l){let p=(0,n.resolveComponent)("button");return(0,n.openBlock)(),(0,n.createElementBlock)("scroll-view",{scrollY:!0,showScrollbar:!0,enableBackToTop:!0,bubble:"true",style:{flexDirection:"column"}},[(0,n.createElementVNode)("div",null,[(0,n.createVNode)(p,{onClick:e[0]||(e[0]=s=>l.openFileSelector(0))},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u8272\u60C5\u68C0\u6D4B")]),_:1}),(0,n.createVNode)(p,{onClick:e[1]||(e[1]=s=>l.openFileSelector(1))},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u4EBA\u8138\u68C0\u6D4B")]),_:1}),(0,n.createVNode)(p,{onClick:e[2]||(e[2]=s=>l.openFileSelector(2))},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u4EBA\u8138\u5BF9\u6BD4")]),_:1}),(0,n.createVNode)(p,{onClick:l.liveness},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u6D3B\u4EBA\u68C0\u6D4B")]),_:1},8,["onClick"])])])}var u=D(k,[["render",N]]);var h=plus.webview.currentWebview();if(h){let t=parseInt(h.id),e="pagesPub/tflite-view",o={};try{o=JSON.parse(h.__query__)}catch(a){}u.mpType="page";let i=Vue.createPageApp(u,{$store:getApp({allowDefault:!0}).$store,__pageId:t,__pagePath:e,__pageQuery:o});i.provide("__globalStyles",Vue.useCssStyles([...__uniConfig.styles,...u.styles||[]])),i.mount("#root")}})();
