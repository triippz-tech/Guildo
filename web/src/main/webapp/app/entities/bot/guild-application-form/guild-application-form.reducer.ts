import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IGuildApplicationForm, defaultValue } from 'app/shared/model/bot/guild-application-form.model';

export const ACTION_TYPES = {
  FETCH_GUILDAPPLICATIONFORM_LIST: 'guildApplicationForm/FETCH_GUILDAPPLICATIONFORM_LIST',
  FETCH_GUILDAPPLICATIONFORM: 'guildApplicationForm/FETCH_GUILDAPPLICATIONFORM',
  CREATE_GUILDAPPLICATIONFORM: 'guildApplicationForm/CREATE_GUILDAPPLICATIONFORM',
  UPDATE_GUILDAPPLICATIONFORM: 'guildApplicationForm/UPDATE_GUILDAPPLICATIONFORM',
  DELETE_GUILDAPPLICATIONFORM: 'guildApplicationForm/DELETE_GUILDAPPLICATIONFORM',
  SET_BLOB: 'guildApplicationForm/SET_BLOB',
  RESET: 'guildApplicationForm/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGuildApplicationForm>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type GuildApplicationFormState = Readonly<typeof initialState>;

// Reducer

export default (state: GuildApplicationFormState = initialState, action): GuildApplicationFormState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GUILDAPPLICATIONFORM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GUILDAPPLICATIONFORM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GUILDAPPLICATIONFORM):
    case REQUEST(ACTION_TYPES.UPDATE_GUILDAPPLICATIONFORM):
    case REQUEST(ACTION_TYPES.DELETE_GUILDAPPLICATIONFORM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GUILDAPPLICATIONFORM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GUILDAPPLICATIONFORM):
    case FAILURE(ACTION_TYPES.CREATE_GUILDAPPLICATIONFORM):
    case FAILURE(ACTION_TYPES.UPDATE_GUILDAPPLICATIONFORM):
    case FAILURE(ACTION_TYPES.DELETE_GUILDAPPLICATIONFORM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDAPPLICATIONFORM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDAPPLICATIONFORM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GUILDAPPLICATIONFORM):
    case SUCCESS(ACTION_TYPES.UPDATE_GUILDAPPLICATIONFORM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GUILDAPPLICATIONFORM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/guild-application-forms';

// Actions

export const getEntities: ICrudGetAllAction<IGuildApplicationForm> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GUILDAPPLICATIONFORM_LIST,
  payload: axios.get<IGuildApplicationForm>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IGuildApplicationForm> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDAPPLICATIONFORM,
    payload: axios.get<IGuildApplicationForm>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGuildApplicationForm> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GUILDAPPLICATIONFORM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGuildApplicationForm> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GUILDAPPLICATIONFORM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGuildApplicationForm> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GUILDAPPLICATIONFORM,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
