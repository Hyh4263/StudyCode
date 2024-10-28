// src/utils/chartConfig.ts

interface ChartOptions {
    title: { text: string; left?: string };
    tooltip: { trigger: string; axisPointer?: { type: string } };
    xAxis: { type: string; data: string[] };
    yAxis: { type: string };
    series: { data: number[] | { value: number; name: string }[]; type: string; name?: string; radius?: string; emphasis?: { itemStyle: { shadowBlur: number; shadowOffsetX: number; shadowColor: string } } }[];
}

export const getBarChartOptions = (title: string, categories: string[], data: number[]): ChartOptions => ({
    title: { text: title },
    tooltip: { trigger: "axis", axisPointer: { type: "shadow" } },
    xAxis: { type: "category", data: categories },
    yAxis: { type: "value" },
    series: [{ data, type: "bar" }],
});

export const getLineChartOptions = (title: string, categories: string[], data: number[]): ChartOptions => ({
    title: { text: title },
    tooltip: { trigger: "axis" },
    xAxis: { type: "category", data: categories },
    yAxis: { type: "value" },
    series: [{ data, type: "line" }],
});

export const getPieChartOptions = (title: string, data: { value: number; name: string }[]): ChartOptions => ({
    title: { text: title, left: "center" },
    tooltip: { trigger: "item" },
    legend: { orient: "vertical", left: "left" },
    series: [
        {
            name: "浏览次数",
            type: "pie",
            radius: "50%",
            data,
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: "rgba(0, 0, 0, 0.5)",
                },
            },
        },
    ],
});