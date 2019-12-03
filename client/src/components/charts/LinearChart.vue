<template>
    <div>
        <h2>Wybierz metrykę:  </h2>

        <select  @change="onChange($event)" class="t2e-select-metric" v-model="selected" >
            <option value="" selected disabled> Przoszę wybrać metrykę</option>
			
			<!--
		   <option v-for="metric in metrics"  v-bind:value="metric">
              Metryka: '{{ metric['metric-id']  }}' , Monitor: '{{ metric['monitor-id'] }}'
			</option>
			-->
			<option v-for="(metric) in metrics_keys"  v-bind:value="metric">
                Metryka: '{{ metric  }}'
            </option>
			
        </select>
        v

        <div>
            <div class="actions">
                <button v-if="isHiddenCalendar" @click="setActiveTab" class="btn btn-xs t2e-btn-select-time-15m" :class="{'btn-primary': range == '15m', 'btn-white': range != '15m'}" type="button"  v-on:click="timeRange('15m')"> 15 min </button>
                <button v-if="isHiddenCalendar" @click="setActiveTab" class="btn btn-xs t2e-btn-select-time-30m" :class="{'btn-primary': range == '30m', 'btn-white': range != '30m'}" type="button" v-on:click="timeRange('30m')"> 30 min </button>
                <button v-if="isHiddenCalendar" @click="setActiveTab" class="btn btn-xs t2e-btn-select-time-1h"  :class="{'btn-primary': range == '1h', 'btn-white': range != '1h'}" type="button" v-on:click="timeRange('1h')"> 1 h </button>
                <button v-if="isHiddenCalendar" @click="setActiveTab" class="btn btn-xs t2e-btn-select-time-24h" :class="{'btn-primary': range == '24h', 'btn-white': range != '24h'}" type="button" v-on:click="timeRange('24h')"> 24 h </button>
                <button v-if="isHiddenCalendar" @click="setActiveTab" class="btn btn-xs t2e-btn-select-time-48h" :class="{'btn-primary': range == '48h', 'btn-white': range != '48h'}" type="button" v-on:click="timeRange('48h')"> 48 h </button>
                <button v-if="isHiddenCalendar" @click="setActiveTab" class="btn btn-xs t2e-btn-select-time-range" :class="{'btn-primary': range == 'showRangeSelector', 'btn-white': range != 'showRangeSelector'}" type="button" v-on:click="timeRange('showRangeSelector')"> Zakres </button>
                <input v-if="!isHiddenCalendar"
                       name="range_from"
                       type="text"
                       :placeholder="'Od: ' + [[ dataRange.start ]]"
                       class="input_data_range"
                       :readonly=true
                />
                <input v-if="!isHiddenCalendar"
                       name="range_to"
                       type="text"
                       :placeholder="'Do: ' + [[ dataRange.end ]]"
                       class="input_data_range"
                       :readonly=true
                />
                <button  v-if="!isHiddenCalendar" class="btn btn-xs t2e-btn-select-ok btn-white" type="button" v-on:click="timeRange('selectRange')" > Ok </button>
                <button v-if="!isHiddenCalendar" class="btn btn-xs t2e-btn-select-cancle btn-white" type="button" v-on:click="timeRange('cancle')"> Anuluj </button>
                <v-range-selector  v-if="!isHiddenCalendar"
                                   :start-date.sync="dataRange.start"
                                   :end-date.sync="dataRange.end"
                                   onclick="if(true){}"
                />

                <b-field v-if="!isHiddenCalendar" label="Select time" class="hh-mm-picker max-z-index">
                    <b-timepicker v-model="pickerTime" class="timepicker"
                                  placeholder="Click to select...">

                        <button class="button is-primary"
                                @click="pickerTime = new Date()">
                            <b-icon icon="clock"></b-icon>
                            <span>Now</span>
                        </button>

                        <button class="button is-danger"
                                @click="pickerTime = null">
                            <b-icon icon="close"></b-icon>
                            <span>Clear</span>
                        </button>
                    </b-timepicker>
                </b-field>

            </div>

            <span class="metricsAlerts">{{noDataInfo}}</span>
            <div id="page-navigation" v-if="!isHiddenPagination">
                <button @click=movePages(-1)>Back</button>
                <p>{{Math.floor(this.startRow / this.rowsPerPage) + 1}} z {{Math.ceil(this.blob_samples.length / this.rowsPerPage)}} </p>
                <button @click=movePages(1)>Next</button>
            </div>

            <apexcharts ref="updateChart" height=350 align="left" type="line" :options="chartOptions" :series="series"></apexcharts>

			
            <h8>{{metrics}}</h8>
			<!--
			-->
        </div>

    </div>
</template>

<script>
    import Vue from 'vue'
    import VueApexCharts from 'vue-apexcharts'
    import VRangeSelector from 'vuelendar/components/vl-range-selector';
    import { Field } from 'buefy/dist/components/field'
    import { Timepicker } from 'buefy/dist/components/timepicker'
    import { Icon } from 'buefy/dist/components/icon'
    import 'buefy/dist/buefy.css'
    //import auth from '../../store/getters'
    Vue.component('b-field', Field)
    Vue.component('b-timepicker', Timepicker)
    Vue.component('b-icon', Icon)
    //TODO: GET TOKEN, add limit ?n=1000 for ./measurements?
    //var TOKEN = auth.access_token;
    var TOKEN = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1NTk4MDM4NjEsIm5iZiI6MTU1OTgwMzg2MSwianRpIjoiZTg2OWNlNDYtZTIwNS00YjBjLWJjY2EtMDZjOGQyZWI0YjdhIiwiaWRlbnRpdHkiOiJ0ZXN0IiwiZnJlc2giOmZhbHNlLCJ0eXBlIjoiYWNjZXNzIn0.rQyN_5WPJHOKpKfCsX-sQki8rIoevcLK188TEIiUQyw';
    var host = 'http://localhost:8080'
    var version = 'v1'
    var n = 20
    var url_metrics = host + '/' + version + '/metrics'
    var time= []
    var value = []
    export default {
        name: 'LinearChart',
        props: ['metric'],
        components: {
            apexcharts: VueApexCharts,
            VRangeSelector,
        },
        mounted:function(){
            this.getMetrics() //method will execute at pageload
        },
        methods: {
            showPagination: function(){
                if(Math.ceil(this.blob_samples.length / this.rowsPerPage) < 1){
                    this.isHiddenPagination = true;
                }
                else{
                    this.isHiddenPagination = false;
                }
            },
            movePages: function(amount) {
                var newStartRow = this.startRow + (amount * this.rowsPerPage);
                if (newStartRow >= 0 && newStartRow < this.blob_samples.length) {
                    this.startRow = newStartRow;
                    if(this.rowsPerPage < this.time.length && (this.startRow + this.rowsPerPage)  <= this.time.length) {
                        this.$refs.updateChart.updateOptions({
                            xaxis: {
                                categories: this.time.slice(this.startRow, this.startRow + this.rowsPerPage),
                            },
                            series: [{
                                data: this.value.slice(this.startRow, this.startRow + this.rowsPerPage),
                            }],
                        });
                    }
                    else if(this.rowsPerPage < this.time.length && (this.startRow + this.rowsPerPage) > this.time.length){
                        this.$refs.updateChart.updateOptions({
                            xaxis: {
                                categories: this.time.slice(this.startRow, this.time.length),
                            },
                            series: [{
                                data: this.value.slice(this.startRow, this.value.length),
                            }],
                        });
                    }
                    else{
                        this.$refs.updateChart.updateOptions({
                            xaxis: {
                                categories: this.time,
                            },
                            series: [{
                                data: this.value,
                            }],
                        });
                    }
                }
            },
            setActiveTab(){
                let _this = this;
                _this.activeTab = 1;
            },
            convertDate(dateString) {
                // dateString = ""+dateString;//.toString();
                var p = dateString.split(/\D+/g)
                return [p[2],p[1],p[0] ].join("/")
            },
            timeRange: function(range) {
                this.beforeDestroy();
                if(!(this.selectedMetric == '' || this.selectedMetric == null || this.selectedMetric == undefined)) {
                    this.isHiddenCalendar = true;
                    var url = host + '/' + version + '/metrics/' + this.selectedMetric + '/measurements?from=';  //+ '+'&n='+ n
                    var dateFormat = require('dateformat');
                    var now = new Date();
                    dateFormat(now, "DD/mm/YYYY HH:MM:SS");
                    // default Data(): "Fri May 31 2019 15:19:41 GMT+0200 (Central European Summer Time)"
                    // expected: "31/05/2019 15:34:41"
                    var hour
                    if (range == '15m') {
                        var _15min = new Date(Date.now() - 1000 * 60 * 15);
                        hour  = ("0" + _15min.getHours()).slice(-2);
                        _15min = dateFormat(_15min, "dd/mm/yyyy "+ hour +":MM:ss");
                        this.draw(url + _15min);
                        this.range = '15m';
                        this.interval = setInterval(function () {
                            _15min = new Date(Date.now() - 1000 * 60 * 15);
                            hour  = ("0" + _15min.getHours()).slice(-2);
                            _15min = dateFormat(_15min, "dd/mm/yyyy "+ hour +":MM:ss");
                            this.draw(url + _15min);
                        }.bind(this), 8000);
                    }
                    else if (range == '30m') {
                        var _30min = new Date(Date.now() - 1000 * 60 * 30);
                        hour  = ("0" + _30min.getHours()).slice(-2);
                        _30min = dateFormat(_30min, "dd/mm/yyyy "+ hour +":MM:ss");
                        this.draw(url + _30min);
                        this.range = '30m';
                    }
                    else if (range == '1h') {
                        var _1h = new Date(Date.now() - 1000 * 60 * 60 * 1);
                        hour  = ("0" + _1h.getHours()).slice(-2);
                        _1h = dateFormat(_1h, "dd/mm/yyyy "+ hour +":MM:ss");
                        this.draw(url + _1h);
                        this.range = '1h';
                    }
                    else if (range == '24h') {
                        var _24h = new Date(Date.now() - 1000 * 60 * 60 * 24);
                        hour  = ("0" + _24h.getHours()).slice(-2);
                        _24h = dateFormat(_24h, "dd/mm/yyyy "+ hour +":MM:ss");
                        this.draw(url + _24h);
                        this.range = '24h';
                    }
                    else if (range == '48h') {
                        var _48h = new Date(Date.now() - 1000 * 60 * 60 * 48);
                        hour  = ("0" + _48h.getHours()).slice(-2);
                        _48h = dateFormat(_48h, "dd/mm/yyyy "+ hour +":MM:ss");
                        this.draw(url + _48h);
                        this.range = '48h';
                    }
                    else if (range == 'showRangeSelector') {
                        this.isHiddenCalendar = false;
                    }//selectRange
                    else if (range == 'selectRange') {
                        hour  = ("0" + this.pickerTime.getHours()).slice(-2);
                        var _start = this.convertDate("" + this.dataRange.start) + " "+ hour + ":" + this.pickerTime.getMinutes()+":00";//+ this.pickerTime.getSeconds();//" 00:00:00";
                        var _end = this.convertDate("" + this.dataRange.end) + " "+ this.pickerTime.getHours() + ":" + this.pickerTime.getMinutes()+":00";//+ this.pickerTime.getSeconds();
                        this.range = 'selectRange';
                        this.draw(url + _start + '&to=' + _end);
                        this.isHiddenCalendar = true;
                        this.range = 'showRangeSelector';
                    }
                    else if (range == 'cancle') {
                        this.isHiddenCalendar = true;
                        //default: nothing
                    }
                    else {
                        this.noDataInfo = 'Wybierz zakres czasu'
                    }
                }
                else{
                    this.noDataInfo = 'Proszę wybrać metrykę, aby zobaczyć jej pomiary'
                }
            },
            draw(url){
                if(!(this.selectedMetric == '' || this.selectedMetric == null || this.selectedMetric == undefined)) {
                    this.range = '15m';//default
                    this.isHiddenCalendar = true;
                    this.$http.get(url, {headers: {'access-token': TOKEN}}).then(function (data) {
                        this.blob_samples = data.body;//.slice(0, 10);
                        this.showPagination();
                        if (data.body == '' || data.body == '[]' || data.body == null || data.body == []) {
                            this.value = [];
                            this.time = [];
                            this.noDataInfo = 'Brak danych'
                        } else {
                            var index;
                            for (index = 0; index < this.blob_samples.length; ++index) {
                                this.value[index] = parseFloat(this.blob_samples[this.blob_samples.length - index - 1].val),
                                    this.time[index] = this.blob_samples[this.blob_samples.length - index - 1].ts
                            }
                            this.noDataInfo = ''
                        }
                        if(this.rowsPerPage < this.time.length) {
                            this.$refs.updateChart.updateOptions({
                                xaxis: {
                                    categories: this.time.slice(0, this.rowsPerPage),
                                },
                                series: [{
                                    data: this.value.slice(0, this.rowsPerPage),
                                }],
                            });
                        }
                        else{
                            this.$refs.updateChart.updateOptions({
                                xaxis: {
                                    categories: this.time,
                                },
                                series: [{
                                    data: this.value,
                                }],
                            });
                        }
                    });
                }
            },
            onChange(event) {
                this.selectedMetric = event.target.value;
                this.created();
            },
            getMetrics: function(){
                this.value = [];
                this.time = [];
                this.$http.get(url_metrics ,  { headers: {'access-token': TOKEN}}).then(function (data) {
                    this.metrics= data.body.metrics;
                    var index;
                    for (index = 0; index < this.metrics.length; ++index) {
                        this.metrics_keys[index]  = this.metrics[index]["metric-id"];
                        this.monitor_keys[index]  = this.metrics[index]["monitor-id"];
                    }
                });
            },
            created: function() {
                this.getMetrics();
                this.timeRange('15m')
            },
            beforeDestroy: function(){
                clearInterval(this.interval);
            }
        },
        data() {
            return {
                chartOptions: {
                    chart: {
                        id: 'basic-bar',
                        toolbar: {
                            show: false,
                        },
                    },
                    markers: {
                        colors: ['#F44336'],
                        size: 2
                    },
                    colors: ['#faaf40'],
                    tooltip: {
                        followCursor: false,
                        theme: 'dark',
                        x: {
                            show: false
                        },
                        marker: {
                            show: false
                        }
                    },
                    xaxis: {
                        categories: time,//.slice(0,10),
                        labels: {
                            style: {
                                colors: '#efefef',
                            },
                        },
                    },
                    yaxis: {
                        decimalsInFloat: 2,
                        labels: {
                            style: {
                                color: '#efefef',
                            },
                        },
                    },
                    grid: {
                        row: {
                            colors: ['#676767', 'transparent'],
                            opacity: 0.4
                        },
                        column: {
                            colors: ['#343434']
                        }
                    },
                    dataLabels: {
                        enabled: false,
                        colors: ['#1Ca710']
                    },
                    stroke: {
                        curve: 'straight'//smooth
                    },
                },
                series: [{
                    name: 'value',
                    data: value,//.slice(0,10)
                }],
                time: [],
                value: [],
                blob_samples: [],
                metrics: [],
                metrics_keys: [],
                monitor_keys : [],
                selected: '',
                selectedMetric: [],
                d: [],
                noDataInfo: '',
                activeTab: 0,
                range: '',//default
                dataRange: {},
                date: null,
                isHiddenCalendar: true,
                isHiddenPagination: true,
                pickerTime: new Date(),
                rowsPerPage: n,
                startRow: 0,
                mounted: function () {
                    this.onChange(event)
                    this.getMetrics()
                    this.created()
                    this.showPagination()
                },
            }
        }
    }
</script>

<style>
    #page-navigation {
        display: flex;
        margin-top: 0px;
        margin-left: 40px;
    }
    #page-navigation p {
        margin-left: 5px;
        margin-right: 5px;
    }
    #page-navigation button {
        background-color: #faaf40;
        border-color: #faaf40;
        color: rgba(255, 255, 255, 0.66);
    }
    .max-z-index{
        z-index: 10000000;
    }
    .hh-mm-picker{
        height: 20px;
        width: 200px;
        margin-top: 0px;
        line-height: 0px;
        /*height: 40%;*/
    }
    .timepicker{
        height: 20px;
        z-index: 10000000;
    }
    .metricsAlerts{
        text-align: center;
        margin-top: 25px;
        color: #ee7d00;
        padding: 30px;
        position: relative;
    }
    select{
        cursor:pointer;
    }
    .t2e-select-metric{
        background-color: #898989;
        padding: 15px;
        font-size: 14px;
    }
    .input_data_range{
        width: 20%;
        height: 40%;
        background: #fff;
        font-size: 12px;
        line-height: 1.5;
        margin-right: 10px;
        padding-left: 6px;
        padding-right: 10px;
        padding-top: 13px;
        text-align: left;
    }
    .actions{
        text-align: left;
        box-sizing: border-box;
        color: rgb(103, 106, 108);
        display: block;
        font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 13px;
        height: 27px;
        line-height: 19px;
        text-size-adjust: 100%;
        width: 800px;
        margin-left: 40px;
        margin-top: 30px;
    }
    .btn-white {
        background: #fff;
        border: 1px solid #e7eaec;
    }
    .btn-primary{
        background-color: #faaf40;
        border-color: #faaf40;
        color: #fff;
        margin-bottom: 15px;
    }
    .btn{
        margin-bottom: 15px;
        border-radius: 3px;
        display: inline-block;
        font-weight: 400;
        text-align: center;
        vertical-align: middle;
        touch-action: manipulation;
        cursor: pointer;
        background-image: none;
        border: 1px solid transparent;
        white-space: nowrap;
        user-select: none;
    }
    .btn-xs{
        padding: 1px 5px;
        font-size: 12px;
        line-height: 1.5;
        margin-bottom: 15px;
    }
    button {
        align-items: flex-start;
        background-color: rgb(221, 221, 221);
        border-bottom-color: rgb(221, 221, 221);
        border-bottom-left-radius: 0px;
        border-bottom-right-radius: 0px;
        border-bottom-style: outset;
        border-bottom-width: 2px;
        border-image-outset: 0px;
        border-image-repeat: stretch;
        border-image-slice: 100%;
        border-image-source: none;
        border-image-width: 1;
        border-left-color: rgb(221, 221, 221);
        border-left-style: outset;
        border-left-width: 2px;
        border-right-color: rgb(221, 221, 221);
        border-right-style: outset;
        border-right-width: 2px;
        border-top-color: rgb(221, 221, 221);
        border-top-left-radius: 0px;
        border-top-right-radius: 0px;
        border-top-style: outset;
        border-top-width: 2px;
        box-sizing: border-box;
        color: rgb(103, 106, 108);
        cursor: pointer;
        display: inline-block;
        font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 13px;
        font-stretch: 100%;
        font-style: normal;
        font-variant-caps: normal;
        font-variant-east-asian: normal;
        font-variant-ligatures: normal;
        font-variant-numeric: normal;
        font-weight: 400;
        height: 24px;
        letter-spacing: normal;
        line-height: 18.5714px;
        margin-bottom: 0px;
        margin-left: 0px;
        margin-right: 10px;
        margin-top: 0px;
        overflow-x: visible;
        overflow-y: visible;
        padding-bottom: 1px;
        padding-left: 6px;
        padding-right: 6px;
        padding-top: 1px;
        text-align: center;
        text-indent: 0px;
        text-rendering: auto;
        text-shadow: none;
        text-size-adjust: 100%;
        text-transform: none;
        width: 55.0156px;
        word-spacing: 0px;
        writing-mode: horizontal-tb;
    }
    div {
        display: block;
    }
    div:after, .actions:after, .btn:after,
    div:before, .actions:before, .btn:before {
        content: none;
    }
    *, ::before, ::after {
        -webkit-box-sizing: unset;
    }
</style>