<template>
    <div>
        <String
            label="EbookId"
            v-model="value.ebookId"
            :editMode="editMode"
        />
        <String
            label="Title"
            v-model="value.title"
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
            label="CoverImage"
            v-model="value.coverImage"
            :editMode="editMode"
        />
        <String
            label="Summary"
            v-model="value.summary"
            :editMode="editMode"
        />
        <Boolean
            label="IsPublicationApproved"
            v-model="value.isPublicationApproved"
            :editMode="editMode"
        />
        <String
            label="PublicationStatus"
            v-model="value.publicationStatus"
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
    name: 'EBook',
    mixins:[BaseEntity],
    components:{
    },
    
    data: () => ({
        path: "eBooks",
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
