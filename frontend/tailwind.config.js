/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{ts,tsx}'],
  darkMode: 'class',
  theme: {
    extend: {
      fontFamily: {
        sans: ['Inter', 'ui-sans-serif', 'system-ui', 'sans-serif'],
      },
      colors: {
        bg: {
          DEFAULT:  'rgb(var(--bg) / <alpha-value>)',
          surface:  'rgb(var(--bg-surface) / <alpha-value>)',
          elevated: 'rgb(var(--bg-elevated) / <alpha-value>)',
        },
        line: 'rgb(var(--line) / <alpha-value>)',
        ink: {
          DEFAULT: 'rgb(var(--ink) / <alpha-value>)',
          muted:   'rgb(var(--ink-muted) / <alpha-value>)',
          dim:     'rgb(var(--ink-dim) / <alpha-value>)',
        },
        brand: {
          DEFAULT: '#7c5cff',
          50:  '#f1edff',
          500: '#7c5cff',
          600: '#6644ff',
          700: '#5435e8',
        },
        success: '#22c55e',
        warning: '#f59e0b',
        danger:  '#ef4444',
      },
      borderRadius: {
        xl: '0.875rem',
      },
      boxShadow: {
        glow: '0 0 0 1px rgba(124,92,255,0.35), 0 8px 30px -12px rgba(124,92,255,0.5)',
      },
    },
  },
  plugins: [],
};
