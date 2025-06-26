<template>
    <div>
        <String
            label="EbookId"
            v-model="value.ebookId"
            :editMode="editMode"
        />
        <String
            label="AuthorId"
            v-model="value.authorId"
            :editMode="editMode"
        />
        <String
            label="ContentId"
            v-model="value.contentId"
            :editMode="editMode"
        />
        <String
            label="AiGeneratedCover"
            v-model="value.aiGeneratedCover"
            :editMode="editMode"
        />
        <String
            label="EbookStatus"
            v-model="value.ebookStatus"
            :editMode="editMode"
        />
        <Date
            label="RegisteredAt"
            v-model="value.registeredAt"
            :editMode="editMode"
        />
        <v-row class="ma-0 pa-0">
            <v-spacer></v-spacer>
            <v-btn width="64px" color="primary" @click="save">
                저장
            </v-btn>
        </v-row>
    </div>
</template>


<script>
import BaseEntity from './base-ui/BaseEntity.vue'

export default {
    name: 'EBookPlatform',
    mixins:[BaseEntity],
    components:{
    },
    
    data: () => ({
        path: "eBookPlatforms",
        value: {
        }
    }),
    created(){
    },
    computed:{
    },
    methods: {
    },
}
</script>
