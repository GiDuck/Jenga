
window.REGEX_PASSWORD = /^(?=.*[a-zA-Z])((?=.*\d)|(?=.*\W)).{8,16}$/;
window.REGEX_EMAIL = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
window.REGEX_TRIM_DIM_EXTEND = /[^-\d\.]/g;
window.REGEX_TRIM_VOID = /\s/gi;
window.REGEX_ONLY_CHAR_AND_NUM = /^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$/;
window.REGEX_TRIM_DIM_SIZE_EXTEND = /(?=\D)(?=^\.)/gi;