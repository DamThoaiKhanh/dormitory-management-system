export const delayMs = (ms: number): Promise<void> => {
  return new Promise((resolve) => setTimeout(resolve, ms));
};

export const delaySecond = (second: number): Promise<void> => {
  return new Promise((resolve) => setTimeout(resolve, second * 1000));
};
