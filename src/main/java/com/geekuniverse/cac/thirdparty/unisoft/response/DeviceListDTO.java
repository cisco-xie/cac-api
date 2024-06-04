package com.geekuniverse.cac.thirdparty.unisoft.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 设备列表
 *
 * @name: DeviceListDTO
 * @author: 谢诗宏
 * @date: 2023-05-16 16:59
 **/
@NoArgsConstructor
@Data
public class DeviceListDTO {
    private Integer code;
    private List<DataDTO> data;
    private String msg;
    private Integer pageIndex;
    private Integer pageCount;
    private Integer isMore;
    private Integer total;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private Integer id;
        private String key;
        private String name;
        private String remark;
        private ProductDTO product;
        private GroupDTO group;
        private StatusDTO status;
        private OnlineDTO online;
        private Integer create;

        @NoArgsConstructor
        @Data
        public static class ProductDTO {
            private Integer id;
            private String key;
            private String title;
        }

        @NoArgsConstructor
        @Data
        public static class GroupDTO {
            private Integer id;
            private String title;
        }

        @NoArgsConstructor
        @Data
        public static class StatusDTO {
            private String power1;
            private String power2;
        }

        @NoArgsConstructor
        @Data
        public static class OnlineDTO {
            private Integer state;
            private String ip;
            private Integer time;
        }
    }
}
