FROM nginx:alpine AS builder

ENV NGINX_DYNAMIC_MODULE_VERSION=0.1.0

RUN apk add --no-cache --virtual .build-deps \
    curl \
    gcc \
    libc-dev \
    make \
    pcre-dev \
    zlib-dev \
    openssl-dev

RUN curl -sL https://github.com/cubicdaiya/ngx_http_upstream_dynamic_module/archive/v${NGINX_DYNAMIC_MODULE_VERSION}.tar.gz | tar xz \
    && cd ngx_http_upstream_dynamic_module-${NGINX_DYNAMIC_MODULE_VERSION} \
    && nginx_src=$(nginx -V 2>&1 | grep -oP 'nginx_path="\K[^"]+') \
    && cd $nginx_src \
    && ./configure --with-compat --add-dynamic-module=../ngx_http_upstream_dynamic_module-${NGINX_DYNAMIC_MODULE_VERSION} \
    && make modules \
    && mkdir -p /etc/nginx/modules \
    && cp objs/ngx_http_upstream_dynamic_module.so /etc/nginx/modules

FROM nginx:alpine

COPY --from=builder /etc/nginx/modules/ngx_http_upstream_dynamic_module.so /etc/nginx/modules/

RUN echo 'load_module modules/ngx_http_upstream_dynamic_module.so;' > /etc/nginx/modules/load_dynamic_module.conf
