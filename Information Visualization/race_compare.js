import {race} from './data.js';
import {htmlLegendPlugin} from "./html_legend.js";

for (let row of race) {
  row.total = row["Asian"] + row["White, non-Hispanic"]+row["Black, non-Hispanic"]+row["Hispanic"]+row["Native American"]+row["Other"];
}

race.sort((rowOne, rowTwo) => {
  return rowTwo.total - rowOne.total;
});

const data = {
  datasets: [
  {
    data: race,
    label: 'Asian',
    borderColor: "#eb4f34",
    parsing: {
      xAxisKey: 'State',
      yAxisKey: 'Asian'
    }
  },
  {
    data: race,
    label: 'Black, non-Hispanic',
    borderColor: "#ffed9e",
    parsing: {
      xAxisKey: 'State',
      yAxisKey: 'Black, non-Hispanic'
    }
  },
  {
    data: race,
    label: 'Hispanic',
    borderColor: "#f7a81e",
    parsing: {
      xAxisKey: 'State',
      yAxisKey: 'Hispanic'
    }
  },
  {
    data: race,
    label: 'Native American',
    borderColor: "#af1ef7",
    parsing: {
      xAxisKey: 'State',
      yAxisKey: 'Native American'
    }
  },
  {
    data: race,
    label: 'Other',
    borderColor: "#6af71e",
    parsing: {
      xAxisKey: 'State',
      yAxisKey: 'Other'
    }
  },
  {
    data: race,
    label: 'White, non-Hispanic',
    borderColor: "#1ea8f7",
    parsing: {
      xAxisKey: 'State',
      yAxisKey: 'White, non-Hispanic'
    }
  },

  ]
};

const config = {
  type: 'line',
  data: data,
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
          text: "Total Incidents",
        }
      }
    },
    animation: {
    },
    plugins: [htmlLegendPlugin]
  }
};

export {config};
