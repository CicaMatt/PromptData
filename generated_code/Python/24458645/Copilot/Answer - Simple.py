import LabelEncoder

def label_encode_dataframe(df):
    le = LabelEncoder()
    for column in df.columns:
        df[column] = le.fit_transform(df[column])
    return df

# Usage
df_encoded = label_encode_dataframe(df)
print(df_encoded)
