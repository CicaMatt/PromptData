from data_analysis.experiment_analysis import split_csv_by_column_names, calculate_ratios_per_question, \
    count_all_cwe_ids, calculate_mean, most_frequent_cwe, count_cq_per_column, check_normality_per_column, \
    wilcoxon_test_ratio

complete_csv = "data/dataset_complete.csv"
ratios_csv = "data/ratios.csv"
cwe_rate_csv = "data/cwe_rate.csv"
cq_rate_csv = "data/cq_rate.csv"
cwe_rate_mean_csv = "data/cwe_rate_mean.csv"
cq_rate_mean_csv = "data/cq_rate_mean.csv"


def ratios_calculation():
    # Calculates #CWE/LOC e #CQ/LOC rates for each code snippet
    calculate_ratios_per_question(complete_csv, ratios_csv)
    # Split the ratios file into a #CWE/LOC rate file and a #CQ/LOC rate file
    split_csv_by_column_names(ratios_csv, cwe_rate_csv, cq_rate_csv)


def mean_calculation():
    # Calculate mean of #CWE/LOC rate across all columns
    calculate_mean(cwe_rate_csv, cwe_rate_mean_csv)
    # Calculate mean of #CQ/LOC rate across all columns
    calculate_mean(cq_rate_csv, cq_rate_mean_csv)

def security_reports():
    # General report about CWE
    count_all_cwe_ids(complete_csv)
    # Report on the most frequent CWE IDs
    # The 'considered_column' parameter allow to filter the result based on the model or the prompt level
    most_frequent_cwe(complete_csv, "cwe_id")


def quality_report():
    count_cq_per_column(complete_csv)


def statistical_tests():
    check_normality_per_column(complete_csv)
    wilcoxon_test_ratio(cwe_rate_csv, "cwe_loc")
    wilcoxon_test_ratio(cq_rate_csv, "cq_loc")


