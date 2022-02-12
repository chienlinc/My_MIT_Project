import {guns, arms, handgun_months, longgun_months, other_months, total_months, incident_months} from './data.js';
import {htmlLegendPlugin} from "./html_legend.js";

const data = {
  datasets: [
  {
    data: guns,
    label: 'Handgun',
    backgroundColor: '#ff5cdc',
    parsing: {
      xAxisKey: 'State',
      yAxisKey: 'Handgun'
    }
  },
  {
    data: guns,
    label: 'Long Gun',
    backgroundColor: "#1ed7f7",
    parsing: {
      xAxisKey: 'State',
      yAxisKey: 'Long Gun'
    }
  },
  {
    data: guns,
    label: 'Other',
    backgroundColor: "#f7e21e",
    parsing: {
      xAxisKey: 'State',
      yAxisKey: 'Other'
    }
  },
  ]
};

const config = {
  type: 'bar',
  data: data,
  options: {
    scales: {
      x: {
        stacked: true,
        grid: {
        },
        ticks: {
        },
        title: {
        }
      },
      y: {
        stacked: true,
        grid: {
        },
        ticks: {
        },
        title: {
          display: true,
          text: "Number of Firearms",
        }
      }
    },
    animation: {
    },
    plugins: [htmlLegendPlugin],
    onClick: e => {

      let canvasPosition = Chart.helpers.getRelativePosition(e, e.chart);
      let barNumber = e.chart.scales.x.getValueForPixel(canvasPosition.x);
      let row = guns[barNumber];

      destroy();

      var ctx = document.getElementById('gun').getContext('2d');

      myChart = new Chart(ctx, {
        type: 'radar',
        data: {
          labels: ['Returned/Disposition', 'Private Sale', 'Returned to Seller - Private Sale',  'Rental', 'Pre-pawn', 'Redemption'],
          datasets: [
          {
            data: [row['Returned Handgun'], row['Private Sale Handgun'], row['Return To Seller Handgun'],  row['Rentals Handgun'], row['Prepawn Handgun'],row['Redemption Handgun']],
            label: row.State + " - Handgun Transaction",
            fill: true,
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: "#f71ea0",
            pointBackgroundColor: "#f71ea0",
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: "#f71ea0"
          },
          {
            data: [row['Returned Long Gun'], row['Private Sale Long Gun'], row['Return To Seller Long Gun'],  row['Rentals Long Gun'], row['Prepawn Long Gun'],row['Redemption Long Gun']],
            label: row.State + " - Long Gun Transaction",
            fill: true,
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: "#1ed7f7",
            pointBackgroundColor: "#1ed7f7",
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: "#1ed7f7"
          },
          {
            data: [row['Returned Other'], row['Private Sale Other'], row['Return To Seller Other'],  0, row['Prepawn Other'],row['Redemption Other']],
            label: row.State + " - Other Transaction",
            fill: true,
            backgroundColor: "#f0f71e",
            borderColor: "#f7e21e",
            pointBackgroundColor: "#f7e21e",
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: "#f7e21e"
          },
        ]
        },
        options: {
          elements: {
            line: {
              borderWidth: 3
            }
          }
        },
      });

      let row1 = arms[barNumber];
      var ctx1 = document.getElementById('arm').getContext('2d');
      myChart1 = new Chart(ctx1, {
        type: 'doughnut',
        data: {
          labels: [row1.State + ' - Armed Gun-related', row1.State + ' - Armed Others', row1.State + ' - Unarmed', row1.State +  ' - Armed Undetermined',row1.State + ' - Armed Unknown Weapon'],
          datasets: [{
            data: [row1['gun related'], row1['others'], row1['unarmed'], row1['undetermined'], row1['unknown weapon']],
            backgroundColor: [
              '#940000',
              '#004299',
              '#009407',
              '#999900',
              '#5c0099',
            ],
            hoverOffset: 4
          }]
        }
      });

      let handgun_m = handgun_months[barNumber];
      let longgun_m = longgun_months[barNumber];
      let other_m = other_months[barNumber];
      let total_m = total_months[barNumber];
      let incident_m = incident_months[barNumber];

      var ctx2 = document.getElementById('months').getContext('2d');
      myChart2 = new Chart(ctx2, {
        type: 'line',
        data: {
          labels: ['January', 'February', 'March', 'April', 'May','June', 'July', 'August', 'September', 'October', 'November', 'December'],
          datasets: [
          {
            data: [handgun_m.January, handgun_m.February, handgun_m.March, handgun_m.April, handgun_m.May, handgun_m.June, handgun_m.July, handgun_m.August, handgun_m.September, handgun_m.October, handgun_m.November, handgun_m.December],
            label: handgun_m.State + ' - Ave. Handgun Count',
            borderColor: '#ff5cdc',
          },
          {
            data: [longgun_m.January, longgun_m.February, longgun_m.March, longgun_m.April, longgun_m.May, longgun_m.June, longgun_m.July, longgun_m.August, longgun_m.September, longgun_m.October, longgun_m.November, longgun_m.December],
            label: longgun_m.State + ' - Ave. Long Gun Count',
            borderColor: "#1ed7f7",
          },
          {
            data: [other_m.January, other_m.February, other_m.March, other_m.April, other_m.May, other_m.June, other_m.July, other_m.August, other_m.September, other_m.October, other_m.November, other_m.December],
            label: other_m.State + ' - Ave. Other Gun Count',
            borderColor:  "#f7e21e",
          },
          {
            data: [total_m.January, total_m.February, total_m.March, total_m.April, total_m.May, total_m.June, total_m.July, total_m.August, total_m.September, total_m.October, total_m.November, total_m.December],
            label: total_m.State + ' - Ave. Total Gun Count',
            borderColor: "#b1de76",
          },
          {
            data: [incident_m.January, incident_m.February, incident_m.March, incident_m.April, incident_m.May, incident_m.June, incident_m.July, incident_m.August, incident_m.September, incident_m.October, incident_m.November, incident_m.December],
            label: incident_m.State + ' - Ave. Incident Count',
            borderColor: "#de7693",
          },
        ]
      },
      options: {
        scales: {
          x: {
            grid: {
            },
            ticks: {
            },
            title: {
            }
          },
          y: {
            grid: {
            },
            ticks: {
            },
            title: {
              display: true,
              text: "Number of Firearms",
            }
          }
        },
      }
      });
    }
  }
};

function destroy(){
  myChart.destroy();
  myChart1.destroy();
  myChart2.destroy();
}

var ctx = document.getElementById('gun').getContext('2d');
var myChart = new Chart(ctx, {
  type: 'radar',
  data: {
    labels: ['Returned/Disposition', 'Private Sale', 'Returned to Seller - Private Sale',  'Rental','Pre-pawn','Redemption'],
    datasets: [
    {
      data: [114, 600, 4,0,0,423],
      label: "California - Handgun Transaction",
      fill: true,
      backgroundColor: 'rgba(255, 99, 132, 0.2)',
      borderColor: "#f71ea0",
      pointBackgroundColor: "#f71ea0",
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: "#f71ea0"
    },
    {
      data: [64, 237, 2, 0,0,423],
      label: "California - Long Gun Transaction",
      fill: true,
      backgroundColor: 'rgba(54, 162, 235, 0.2)',
      borderColor: "#1ed7f7",
      pointBackgroundColor: "#1ed7f7",
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: "#1ed7f7"
    },
    {
      data: [8,48,0,0,0,11],
      label: "California - Other Transaction",
      fill: true,
      backgroundColor: "#f0f71e",
      borderColor: "#f7e21e",
      pointBackgroundColor: "#f7e21e",
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: "#f7e21e"
    },
    ]
  },
  options: {
    elements: {
      line: {
        borderWidth: 3
      }
    }
  },
});

var ctx1 = document.getElementById('arm').getContext('2d');
var myChart1 = new Chart(ctx1, {
  type: 'doughnut',
  data: {
    labels: ['California - Armed Gun-related', 'California - Armed Others', 'California - Unarmed',  'California - Armed Undetermined','California - Armed Unknown Weapon'],
    datasets: [{
      data: [426,413,74,32,10],
      backgroundColor: [
        '#940000',
        '#004299',
        '#009407',
        '#999900',
        '#5c0099',
      ],
      hoverOffset: 4
    }]
  }
});

var ctx2 = document.getElementById('months').getContext('2d');
var myChart2 = new Chart(ctx2, {
  type: 'line',
  data: {
    labels: ['January', 'February', 'March', 'April', 'May','June', 'July', 'August', 'September', 'October', 'November', 'December'],
    datasets: [
    {
      data: [42857,40205,49564,41777,41811,46524,41088,43336,38976,44048,45637,56716],
      label: 'California - Ave. Handgun Count',
      borderColor: '#ff5cdc',
    },
    {
      data: [26482,25926,32910,27309,26304,29552,29392,31821,29235,32208,42136,46723],
      label: 'California - Ave. Long Gun Count',
      borderColor: "#1ed7f7",
    },
    {
      data: [4602,4415,4671,4816,4878,5850,6315,6363,5659,6843,9222,16473],
      label: 'California - Ave. Other Gun Count',
      borderColor: "#f7e21e",
    },
    {
      data: [73942,70546,87146,73902,72992,81925,76795,81520,73870,83099,96995,119912],
      label: 'California - Ave. Total Gun Count',
      borderColor: "#b1de76",
    },
    {
      data: [79,69,94,83,77,84,89,90,58,85,68,79],
      label: 'California - Ave. Incident Count',
      borderColor: "#de7693",
    },
  ]
  },
  options: {
    scales: {
      x: {
        grid: {
        },
        ticks: {
        },
        title: {
        }
      },
      y: {
        grid: {
        },
        ticks: {
        },
        title: {
          display: true,
          text: "Number of Firearms",
        }
      }
    },
  }
});

export {config};
